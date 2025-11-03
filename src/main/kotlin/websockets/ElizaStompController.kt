package websockets.stomp

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import websockets.Eliza
import java.util.Scanner
import java.util.Locale

@Controller
class ElizaStompController {
    private val eliza = Eliza()

    /**
     * Manages messages arriving at /app/chat
     * and answers at /topic/replies
     * @param message
     * @return response
     */
    @MessageMapping("/chat")
    @SendTo("/topic/replies")
    fun processMessage(message: String): String {
        val scanner = Scanner(message.lowercase(Locale.getDefault()))
        val response = eliza.respond(scanner)
        return "$response"
    }
}
