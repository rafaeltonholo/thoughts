package dev.tonholo.study.chatapp.data.remote

import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

sealed class SocketEvent {
    data class ChannelOpened(
        val response: Response,
    ) : SocketEvent()

    sealed class Message: SocketEvent() {
        data class TextMessage(
            val text: String,
        ) : Message()

        class ByteStringMessage(
            val byteString: ByteString,
        ) : Message()
    }

    data class Failure(
        val throwable: Throwable? = null,
    ) : SocketEvent()

    object Aborted : SocketEvent()
}

const val CHANNEL_BUFFER_EVENTS_SIZE = 1

@Singleton
class SocketEventHandler @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) {
    val socketEvents = Channel<SocketEvent>(CHANNEL_BUFFER_EVENTS_SIZE)
    private val scope = CoroutineScope(dispatcher)

    fun onOpen(response: Response) {
        scope.launch {
            socketEvents.send(SocketEvent.ChannelOpened(response))
        }
    }

    fun onMessage(text: String) {
        scope.launch {
            socketEvents.send(SocketEvent.Message.TextMessage(text))
        }
    }

    fun onMessage(bytes: ByteString) {
        scope.launch {
            socketEvents.send(SocketEvent.Message.ByteStringMessage(bytes))
        }
    }

    fun onClosing() {
        scope.launch {
            socketEvents.send(SocketEvent.Aborted)
        }
        socketEvents.close()
        scope.cancel()
    }

    fun onFailure(t: Throwable) {
        scope.launch {
            socketEvents.send(SocketEvent.Failure(t))
        }
    }
}
