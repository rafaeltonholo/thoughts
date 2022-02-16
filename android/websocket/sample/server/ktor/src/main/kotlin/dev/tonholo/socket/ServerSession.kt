package dev.tonholo.socket

import dev.tonholo.data.Connection
import java.util.*

private class InternalWebSocketSession {
    companion object {
        val connections = Collections.synchronizedCollection<Connection?>(LinkedHashSet())

        private val instance by lazy {
            InternalWebSocketSession()
        }
    }

    fun onOpen() {

    }
}

object WebSocketSession : InternalWebSocketSession.Companion.instance
