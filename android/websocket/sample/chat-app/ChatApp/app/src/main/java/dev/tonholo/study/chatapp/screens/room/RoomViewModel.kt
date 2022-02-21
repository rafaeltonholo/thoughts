package dev.tonholo.study.chatapp.screens.room

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManager
import dev.tonholo.study.chatapp.data.dataStore.extensions.getUsername
import dev.tonholo.study.chatapp.data.dataStore.extensions.setUsername
import dev.tonholo.study.chatapp.data.model.NamedRoom
import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import dev.tonholo.study.chatapp.di.coroutine.MainDispatcher
import dev.tonholo.study.chatapp.usecase.ListenToMessagesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "RoomViewModel"

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val listenToMessagesUseCase: ListenToMessagesUseCase,
    private val dataStoreManager: DataStoreManager,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val error = mutableStateOf("")
    val usernameState = mutableStateOf("")
    val rooms = mutableStateListOf<NamedRoom>()
    val requestUsername = mutableStateOf(false)
    private var isConnected = false

    init {
        viewModelScope.launch(mainDispatcher) {
            dataStoreManager.getUsername {
                usernameState.value = it
            }
        }
    }

    fun listenRooms() {
        isConnected = false

        viewModelScope.launch(ioDispatcher) {
            isConnected = listenToMessagesUseCase(
                onChannelOpened = {
                    Log.i(TAG, "onChannelOpened: response = $it")
                },
                onMessage = { text, bytes ->
                    Log.i(TAG, "onMessage: text = $text, bytes = $bytes")
                    text?.let {
                        rooms.clear()
                        rooms.addAll(it.rooms ?: listOf())
                    }
                },
                onError = { throwable, message ->
                    error.value = message
                    Log.e(TAG, "listenRooms: Error", throwable)
                }
            ) is ListenToMessagesUseCase.Result.Listening

            withContext(mainDispatcher) {
                if (!isConnected && usernameState.value.isBlank()) {
                    requestUsername.value = true
                }
            }
        }
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            if (username.isBlank()) {
                error.value = "Username can't be null"
            } else {
                dataStoreManager.setUsername(username)
                usernameState.value = username
                requestUsername.value = false
            }
        }
    }
}
