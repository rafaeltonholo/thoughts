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
import dev.tonholo.study.chatapp.di.coroutine.MainDispatcher
import dev.tonholo.study.chatapp.usecase.EnterRoomUseCase
import dev.tonholo.study.chatapp.usecase.ListenToMessagesUseCase
import dev.tonholo.study.chatapp.usecase.SendMessageUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject

private const val TAG = "ChatViewModel"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val enterRoomUseCase: EnterRoomUseCase,
    private val listenToMessagesUseCase:  ListenToMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val messages = mutableStateListOf<Message>()
    val error = mutableStateOf("")
    val hasNewMessage = mutableStateOf(false)

    fun enterRoom(room: String) {
        enterRoomUseCase(roomName = room)
    }

    fun listenToMessages() {
        viewModelScope.launch(ioDispatcher) {
            listenToMessagesUseCase(
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

                        hasNewMessage.value = true
                    },
                    onError = { throwable, message ->
                        Log.e(TAG, "listenRooms: Error", throwable)
                        withContext(mainDispatcher) {
                            error.value = message
                        }
                    }
                )
        }
    }

    fun readNewMessage() {
        hasNewMessage.value = false
    }

    fun sendMessage(message: String) {
        sendMessageUseCase(message)
    }
}
