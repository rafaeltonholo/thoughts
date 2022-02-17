package dev.tonholo.study.chatapp.screens.room

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.chatapp.data.model.Message
import dev.tonholo.study.chatapp.data.model.NamedRoom
import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import dev.tonholo.study.chatapp.usecase.WebSocketInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject

private const val TAG = "RoomViewModel"

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val webSocketInteractor: WebSocketInteractor,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    val error = mutableStateOf("")
    val rooms = mutableStateListOf<NamedRoom>()

    fun subscribeTo(username: String) {
        viewModelScope.launch(dispatcher) {
            webSocketInteractor
                .start("all", username)
                .listen(
                    onMessage = { text, _ ->
                        text?.let {
                            rooms.clear()
                            rooms.addAll(it.rooms ?: listOf())
                        }
                    },
                    onError = { throwable, message ->
                        error.value = message
                        Log.e(TAG, "subscribeTo: Error", throwable)
                    }
                )
        }
    }
}
