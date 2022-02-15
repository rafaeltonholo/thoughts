package dev.tonholo.study.chatapp.data

import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

sealed class SocketUpdate {
    data class ChannelOpened(
        val response: Response,
    ) : SocketUpdate()

    sealed class Message: SocketUpdate() {
        data class TextMessage(
            val text: String,
        ) : Message()

        class ByteStringMessage(
            val byteString: ByteString,
        ) : Message()
    }

    data class Failure(
        val throwable: Throwable? = null,
    ) : SocketUpdate()

    object Aborted : SocketUpdate()
}

const val CHANNEL_BUFFER_EVENTS_SIZE = 1

class ChatAppWebSocketListener @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : WebSocketListener() {
    private val socketEventChannel = Channel<SocketUpdate>(CHANNEL_BUFFER_EVENTS_SIZE)
    private val scope = CoroutineScope(dispatcher)

    override fun onOpen(webSocket: WebSocket, response: Response) {
        scope.launch {
            socketEventChannel.send(SocketUpdate.ChannelOpened(response))
        }
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        scope.launch {
            socketEventChannel.send(SocketUpdate.Message.TextMessage(text))
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        scope.launch {
            socketEventChannel.send(SocketUpdate.Message.ByteStringMessage(bytes))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        scope.launch {
            socketEventChannel.send(SocketUpdate.Aborted)
        }
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        socketEventChannel.close()
        scope.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        scope.launch {
            socketEventChannel.send(SocketUpdate.Failure(t))
        }
    }
}
