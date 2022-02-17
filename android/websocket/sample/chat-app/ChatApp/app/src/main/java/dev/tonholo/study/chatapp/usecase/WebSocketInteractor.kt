package dev.tonholo.study.chatapp.usecase

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import dagger.hilt.android.scopes.ViewModelScoped
import dev.tonholo.study.chatapp.data.*
import dev.tonholo.study.chatapp.data.model.Message
import kotlinx.coroutines.channels.consumeEach
import okhttp3.Response
import okio.ByteString
import javax.inject.Inject

@ViewModelScoped
class WebSocketInteractor @Inject constructor(
    private val webSocketProviderFactory: WebSocketProviderFactory,
    private val socketEventHandler: SocketEventHandler,
    private val gson: Gson,
) {
    private lateinit var webSocketProvider: WebSocketProvider

    fun start(room: String, username: String): WebSocketInteractor {
        if (::webSocketProvider.isInitialized) return this

        webSocketProvider = webSocketProviderFactory.create(room, username)
        webSocketProvider.startSocket()

        return this
    }

    suspend fun listen(
        onMessage: (message: Message?, bytes: ByteString?) -> Unit,
        onError: (throwable: Throwable?, message: String) -> Unit,
        onChannelOpened: (response: Response) -> Unit = {}
    ) = socketEventHandler.socketEvents.consumeEach { update ->
        when(update) {
            SocketEvent.Aborted -> onError(null, "Socket connection closed")
            is SocketEvent.ChannelOpened -> onChannelOpened(update.response)
            is SocketEvent.Failure ->
                onError(update.throwable, update.throwable?.localizedMessage ?: "Unhandled Exception")
            is SocketEvent.Message.ByteStringMessage -> onMessage(null, update.byteString)
            is SocketEvent.Message.TextMessage -> {
                val message = gson.fromJson(update.text, Message::class.java)
                onMessage(message, null)
            }
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
