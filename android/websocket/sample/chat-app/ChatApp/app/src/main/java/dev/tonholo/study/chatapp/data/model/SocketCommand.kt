package dev.tonholo.study.chatapp.data.model

sealed class SocketCommand {
    class Connect(
        val username: String,
    ): SocketCommand()

    class EnterRoom(
        val roomName: String,
    ): SocketCommand()

    object LeaveRoom: SocketCommand()

    class SendMessage(
        val message: String
    ): SocketCommand()
}
