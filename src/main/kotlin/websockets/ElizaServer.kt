@file:Suppress("NoWildcardImports", "WildcardImport", "SpreadOperator")

package websockets

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.websocket.CloseReason
import jakarta.websocket.CloseReason.CloseCodes
import jakarta.websocket.OnClose
import jakarta.websocket.OnError
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.RemoteEndpoint
import jakarta.websocket.Session
import jakarta.websocket.server.ServerEndpoint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.server.standard.ServerEndpointExporter
import java.util.Locale
import java.util.Scanner
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.concurrent.fixedRateTimer

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Configuration(proxyBeanMethods = false)
class WebSocketConfig {
    @Bean
    fun serverEndpoint() = ServerEndpointExporter()
}

private val logger = KotlinLogging.logger {}

/**
 * If the websocket connection underlying this [RemoteEndpoint] is busy sending a message when a call is made to send
 * another one, for example if two threads attempt to call a send method concurrently, or if a developer attempts to
 * send a new message while in the middle of sending an existing one, the send method called while the connection
 * is already busy may throw an [IllegalStateException].
 *
 * This method wraps the call to [RemoteEndpoint.Basic.sendText] in a synchronized block to avoid this exception.
 */
fun RemoteEndpoint.Basic.sendTextSafe(message: String) {
    synchronized(this) {
        sendText(message)
    }
}

// ---------- SHARED METRICS ----------
object Metrics {
    private val sessions = CopyOnWriteArraySet<Session>()
    private var activeConnections = 0
    private var totalMessages = 0L
    private var lastTotal = 0L

    init {
        // To send the metrics every second
        fixedRateTimer("metrics", initialDelay = 1000, period = 1000) {
            val newTotal = totalMessages
            val perSecond = newTotal - lastTotal
            lastTotal = newTotal

            val metricsJson =
                """
                {
                  "activeConnections": $activeConnections,
                  "totalMessages": $totalMessages,
                  "messagesPerSecond": $perSecond
                }
                """.trimIndent()

            sessions.forEach { session ->
                if (session.isOpen) {
                    synchronized(session.basicRemote) {
                        session.basicRemote.sendText(metricsJson)
                    }
                }
            }
        }
    }

    fun incrementConnections() {
        activeConnections++
    }

    fun decrementConnections() {
        activeConnections--
    }

    fun incrementMessages() {
        totalMessages++
    }

    fun registerDashboard(session: Session) {
        sessions.add(session)
    }

    fun unregisterDashboard(session: Session) {
        sessions.remove(session)
    }
}

@ServerEndpoint("/eliza")
@Component
class ElizaEndpoint {
    private val eliza = Eliza()

    /**
     * Successful connection
     *
     * @param session
     */
    @OnOpen
    fun onOpen(session: Session) {
        Metrics.incrementConnections()
        logger.info { "Server Connected ... Session ${session.id}" }
        with(session.basicRemote) {
            sendTextSafe("The doctor is in.")
            sendTextSafe("What's on your mind?")
            sendTextSafe("---")
        }
    }

    /**
     * Connection closure
     *
     * @param session
     */
    @OnClose
    fun onClose(
        session: Session,
        closeReason: CloseReason,
    ) {
        Metrics.decrementConnections()
        logger.info { "Session ${session.id} closed because of $closeReason" }
    }

    /**
     * Message received
     *
     * @param message
     */
    @OnMessage
    fun onMsg(
        message: String,
        session: Session,
    ) {
        Metrics.incrementMessages()
        logger.info { "Server Message ... Session ${session.id}" }
        val currentLine = Scanner(message.lowercase(Locale.getDefault()))
        if (currentLine.findInLine("bye") == null) {
            logger.info { "Server received \"${message}\"" }
            runCatching {
                if (session.isOpen) {
                    with(session.basicRemote) {
                        sendTextSafe(eliza.respond(currentLine))
                        sendTextSafe("---")
                    }
                }
            }.onFailure {
                logger.error(it) { "Error while sending message" }
                session.close(CloseReason(CloseCodes.CLOSED_ABNORMALLY, "I'm sorry, I didn't understand that."))
            }
        } else {
            session.close(CloseReason(CloseCodes.NORMAL_CLOSURE, "Alright then, goodbye!"))
        }
    }

    @OnError
    fun onError(
        session: Session,
        errorReason: Throwable,
    ) {
        logger.error(errorReason) { "Session ${session.id} closed because of ${errorReason.javaClass.name}" }
    }
}

@ServerEndpoint("/dashboard")
@Component
class DashboardEndpoint {
    @OnOpen
    fun onOpen(session: Session) {
        Metrics.registerDashboard(session)
        logger.info { "Dashboard connected: ${session.id}" }
    }

    @OnClose
    fun onClose(session: Session) {
        Metrics.unregisterDashboard(session)
        logger.info { "Dashboard closed: ${session.id}" }
    }
}
