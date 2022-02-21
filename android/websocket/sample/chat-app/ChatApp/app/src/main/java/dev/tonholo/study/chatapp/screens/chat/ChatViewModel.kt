package dev.tonholo.study.chatapp.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManager
import dev.tonholo.study.chatapp.data.dataStore.extensions.getUsername
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
    private val listenToMessagesUseCase: ListenToMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val dataStoreManager: DataStoreManager,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val messages = mutableStateOf<List<Message>>(listOf())
    val error = mutableStateOf("")
    val hasNewMessage = mutableStateOf(false)
    val usernameState = mutableStateOf("")

    init {
        viewModelScope.launch(mainDispatcher) {
            dataStoreManager.getUsername {
                usernameState.value = it
            }
        }
    }

    fun enterRoom(room: String) {
        enterRoomUseCase(roomName = room)
    }

    fun listenToMessages() {
        viewModelScope.launch(ioDispatcher) {
            listenToMessagesUseCase(
                onMessage = { text, bytes ->
                    val now = Date()
                    text?.let {
                        withContext(mainDispatcher) {
                            messages.value += it.apply {
                                received = now
                            }
                        }
                    }

                    bytes?.let {
                        withContext(mainDispatcher) {
                            messages.value +=
                                Message(
                                    owner = Owner.Unknown,
                                    text = it.string(Charset.defaultCharset()),
                                    received = now,
                                )
                        }
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

    fun confirmNewMessageRead() {
        hasNewMessage.value = false
    }

    fun sendMessage(message: String) {
        sendMessageUseCase(message)
    }
}
