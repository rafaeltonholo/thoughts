package dev.tonholo.study.chatapp.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.chatapp.data.model.Message
import dev.tonholo.study.chatapp.data.model.Owner
import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import dev.tonholo.study.chatapp.usecase.WebSocketInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject

private const val TAG = "ChatViewModel"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val webSocketInteractor: WebSocketInteractor,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    val messages = mutableStateListOf<Message>()
    val error = mutableStateOf("")

    fun subscribeTo(room: String, username: String) {
        viewModelScope.launch(dispatcher) {
            webSocketInteractor
                .start(room, username)
                .listen(
                    onMessage = { text, bytes ->
                        val now = Date()
                        text?.let {
                            messages.add(
                                it.apply {
                                    received = now
                                }
                            )
                        }

                        bytes?.let {
                            messages.add(
                                Message(
                                    owner = Owner.Unknown,
                                    text = it.string(Charset.defaultCharset()),
                                    received = now,
                                )
                            )
                        }
                    },
                    onError = { throwable, message ->
                        error.value = message
                        Log.e(TAG, "subscribeTo: Error", throwable)
                    }
                )
        }
    }

    fun sendMessage(message: String) {
        webSocketInteractor.send(message)
    }

    override fun onCleared() {
        webSocketInteractor.stop()
        super.onCleared()
    }
}
