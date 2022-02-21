package dev.tonholo.study.chatapp.usecase

import dev.tonholo.study.chatapp.data.remote.WebSocketProvider
import dev.tonholo.study.chatapp.data.model.SocketCommand
import javax.inject.Inject

class EnterRoomUseCase @Inject constructor(
    private val webSocketProvider: WebSocketProvider,
) {
    operator fun invoke(roomName: String) {
        val command = SocketCommand.EnterRoom(roomName)
        webSocketProvider.sendCommand(command)
    }
}
