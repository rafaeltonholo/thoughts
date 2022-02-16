package dev.tonholo.plugins

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import dev.tonholo.data.Connection
import dev.tonholo.socket.ServerSession
import dev.tonholo.socket.ServerSession.handleMessages
import java.util.*

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {
        webSocket("/chat/{room}/{username}") {
            ServerSession.onOpen(this)
            ServerSession.handleMessages(this)
        }
    }
}
