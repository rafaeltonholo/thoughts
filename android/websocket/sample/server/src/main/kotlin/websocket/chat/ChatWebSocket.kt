package websocket.chat

import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import org.reactivestreams.Publisher
import java.util.function.Predicate

@ServerWebSocket("/ws/chat/{topic}/{username}")
class ChatWebSocket(private val broadcaster: WebSocketBroadcaster) {
    @OnOpen
    fun onOpen(topic: String, username: String, session: WebSocketSession?): Publisher<String> {
        val msg = "[$username] Joined!"
        return broadcaster.broadcast(msg, isValid(topic))
    }

    @OnMessage
    fun onMessage(
        topic: String,
        username: String,
        message: String,
        session: WebSocketSession?
    ): Publisher<String> {
        val msg = "[$username] $message"
        return broadcaster.broadcast(msg, isValid(topic))
    }

    @OnClose
    fun onClose(
        topic: String,
        username: String,
        session: WebSocketSession?
    ): Publisher<String> {
        val msg = "[$username] Disconnected!"
        return broadcaster.broadcast(msg, isValid(topic))
    }

    private fun isValid(topic: String): Predicate<WebSocketSession> {
        return Predicate { s: WebSocketSession ->
            topic.equals(
                s.uriVariables.get(
                    "topic",
                    String::class.java, null
                ), ignoreCase = true
            )
        }
    }
}