package dev.tonholo.study.chatapp.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import dev.tonholo.study.chatapp.data.*
import kotlinx.coroutines.channels.consumeEach
import okhttp3.Response
import okio.ByteString
import javax.inject.Inject

@ViewModelScoped
class WebSocketInteractor @Inject constructor(
    private val webSocketProviderFactory: WebSocketProviderFactory,
    private val socketEventHandler: SocketEventHandler,
) {
    private lateinit var webSocketProvider: WebSocketProvider

    fun start(room: String, username: String): WebSocketInteractor {
        if (::webSocketProvider.isInitialized) return this

        webSocketProvider = webSocketProviderFactory.create(room, username)
        webSocketProvider.startSocket()

        return this
    }

    suspend fun listen(
        onMessage: (text: String?, bytes: ByteString?) -> Unit,
        onError: (throwable: Throwable?, message: String) -> Unit,
        onChannelOpened: (response: Response) -> Unit = {}
    ) = socketEventHandler.socketEvents.consumeEach { update ->
        when(update) {
            SocketEvent.Aborted -> onError(null, "Socket connection closed")
            is SocketEvent.ChannelOpened -> onChannelOpened(update.response)
            is SocketEvent.Failure ->
                onError(update.throwable, update.throwable?.localizedMessage ?: "Unhandled Exception")
            is SocketEvent.Message.ByteStringMessage -> onMessage(null, update.byteString)
            is SocketEvent.Message.TextMessage -> onMessage(update.text, null)
        }
    }

    fun send(message: String) {
        webSocketProvider.sendMessage(SocketEvent.Message.TextMessage(message))
    }

    fun stop() {
        if (!::webSocketProvider.isInitialized)
            throw IllegalStateException("Can't stop web socket before starting it")

        webSocketProvider.stopSocket()
    }
}
