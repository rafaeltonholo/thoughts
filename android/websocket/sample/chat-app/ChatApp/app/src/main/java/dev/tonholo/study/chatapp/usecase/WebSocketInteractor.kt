package dev.tonholo.study.chatapp.usecase

import dev.tonholo.study.chatapp.data.WebSocketProvider
import dev.tonholo.study.chatapp.data.WebSocketProviderFactory
import javax.inject.Inject

class HandleWebSocketStateUseCase @Inject constructor(
    private val webSocketProviderFactory: WebSocketProviderFactory,
) {
    private lateinit var webSocketProvider: WebSocketProvider

    fun start(room: String, username: String) {
        if (::webSocketProvider.isInitialized) return

        webSocketProvider = webSocketProviderFactory.create(room, username)
        webSocketProvider.startSocket()
    }

    fun stop() {
        if (!::webSocketProvider.isInitialized)
            throw IllegalStateException("Can't stop web socket before starting it")

        webSocketProvider.stopSocket()
    }
}
