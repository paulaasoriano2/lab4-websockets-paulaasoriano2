package websockets

data class WebSocketMetrics(
    var activeConnections: Int = 0,
    var totalMessages: Long = 0,
    var messagesPerSecond: Double = 0.0,
)
