package dev.tonholo.study.chatapp.usecase

import android.util.Log
import dev.tonholo.study.chatapp.data.remote.WebSocketProvider
import javax.inject.Inject

private const val TAG = "OpenConnectionUseCase"
class OpenConnectionUseCase @Inject constructor(
    private val webSocketProvider: WebSocketProvider,
) {
    operator fun invoke(username: String?) {
        if (username.isNullOrBlank()) {
            Log.i(TAG, "invoke: No username provided, skipping")
            return
        }

        if (webSocketProvider.isStarted) {
            Log.i(TAG, "invoke: trying to open an already opened connection. Skipping")
        } else {
            webSocketProvider.startSocket(username)
        }
    }
}
