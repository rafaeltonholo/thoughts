package dev.tonholo.study.chatapp.data

import dagger.hilt.android.scopes.ViewModelScoped
import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Response
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

@ViewModelScoped
class SocketUpdateHandler @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) {
    val socketUpdates = Channel<SocketUpdate>(CHANNEL_BUFFER_EVENTS_SIZE)
    private val scope = CoroutineScope(dispatcher)

    fun onOpen(response: Response) {
        scope.launch {
            socketUpdates.send(SocketUpdate.ChannelOpened(response))
        }
    }

    fun onMessage(text: String) {
        scope.launch {
            socketUpdates.send(SocketUpdate.Message.TextMessage(text))
        }
    }

    fun onMessage(bytes: ByteString) {
        scope.launch {
            socketUpdates.send(SocketUpdate.Message.ByteStringMessage(bytes))
        }
    }

    fun onClosing() {
        scope.launch {
            socketUpdates.send(SocketUpdate.Aborted)
        }
        socketUpdates.close()
        scope.cancel()
    }

    fun onFailure(t: Throwable) {
        scope.launch {
            socketUpdates.send(SocketUpdate.Failure(t))
        }
    }
}
