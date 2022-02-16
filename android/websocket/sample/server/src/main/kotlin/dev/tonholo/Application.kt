package dev.tonholo

import dev.tonholo.plugins.configureRouting
import io.ktor.application.*
import io.ktor.websocket.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(WebSockets)
    configureRouting()
}
