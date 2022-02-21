package dev.tonholo.study.chatapp.usecase

import dev.tonholo.study.chatapp.data.remote.WebSocketProvider
import dev.tonholo.study.chatapp.data.model.SocketCommand
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val webSocketProvider: WebSocketProvider,
) {
    operator fun invoke(message: String) {
        val command = SocketCommand.SendMessage(message)
        webSocketProvider.sendCommand(command)
    }
}
