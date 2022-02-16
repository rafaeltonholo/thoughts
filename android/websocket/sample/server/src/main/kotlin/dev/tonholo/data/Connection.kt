package dev.tonholo.data

import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.concurrent.atomic.AtomicInteger

@Serializable
sealed class Connection {
    @Serializable
    object Server: Connection() {
        const val id = -1
        const val name = "Broadcast Server"
    }

    @Serializable
    data class User(
        @Transient val session: WebSocketServerSession? = null,
        val name: String,
        val id: Int = lastId.getAndIncrement(),
        @Transient var currentRoom: Room = Room.All,
    ): Connection() {
        companion object {
            val lastId = AtomicInteger(0)
        }
    }
}
