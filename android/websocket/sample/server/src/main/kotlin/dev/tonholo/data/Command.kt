package dev.tonholo.data

import kotlinx.serialization.Serializable

@Serializable
sealed class Command {
    @Serializable
    class Connect(
        val username: String,
    ): Command()

    @Serializable
    class EnterRoom(
        val roomName: String,
    ): Command()

    @Serializable
    object LeaveRoom: Command()

    @Serializable
    class SendMessage(
        val message: String
    ): Command()
}
