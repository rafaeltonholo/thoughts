package dev.tonholo.study.chatapp.data.remote

import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

const val CHANNEL_BUFFER_EVENTS_SIZE = 10

@Singleton
class SocketEventHandler @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) {
    private val _socketEvents = MutableSharedFlow<SocketEvent>(CHANNEL_BUFFER_EVENTS_SIZE)
    val socketEvents = _socketEvents.asSharedFlow()
    private val scope = CoroutineScope(dispatcher)

    fun onOpen(response: Response) {
        scope.launch {
            _socketEvents.emit(SocketEvent.ChannelOpened(response))
        }
    }

    fun onMessage(text: String) {
        scope.launch {
            _socketEvents.emit(SocketEvent.Message.TextMessage(text))
        }
    }

    fun onMessage(bytes: ByteString) {
        scope.launch {
            _socketEvents.emit(SocketEvent.Message.ByteStringMessage(bytes))
        }
    }

    fun onClosing() {
        scope.launch {
            _socketEvents.emit(SocketEvent.Aborted)
        }
        scope.cancel()
    }

    fun onFailure(t: Throwable) {
        scope.launch {
            _socketEvents.emit(SocketEvent.Failure(t))
        }
    }
}
