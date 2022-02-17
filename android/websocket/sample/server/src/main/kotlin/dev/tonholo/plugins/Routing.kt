package dev.tonholo.plugins

import dev.tonholo.socket.ServerSession
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {
        webSocket("/chat/{username}") {
            ServerSession.onOpen(this)
            ServerSession.handleMessages(this)
        }
    }
}
