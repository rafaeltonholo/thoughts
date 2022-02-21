package dev.tonholo.study.chatapp.usecase

import com.google.gson.Gson
import dev.tonholo.study.chatapp.data.remote.SocketEvent
import dev.tonholo.study.chatapp.data.remote.SocketEventHandler
import dev.tonholo.study.chatapp.data.model.Message
import dev.tonholo.study.chatapp.data.remote.WebSocketProvider
import kotlinx.coroutines.channels.consumeEach
import okhttp3.Response
import okio.ByteString
import javax.inject.Inject

class ListenToMessagesUseCase @Inject constructor(
    private val webSocketProvider: WebSocketProvider,
    private val socketEventHandler: SocketEventHandler,
    private val gson: Gson,
) {

    sealed class Result {
        object NotOpenedConnection: Result()
        object Listening: Result()
    }

    suspend operator fun invoke(
        onMessage: suspend (message: Message?, bytes: ByteString?) -> Unit,
        onError: suspend (throwable: Throwable?, message: String) -> Unit,
        onChannelOpened: suspend (response: Response) -> Unit = {}
    ): Result {
        if (!webSocketProvider.isStarted) {
            return Result.NotOpenedConnection
        }

        socketEventHandler.socketEvents.consumeEach { update ->
            when (update) {
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

        return Result.Listening
    }
}
