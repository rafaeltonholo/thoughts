package dev.tonholo.study.chatapp.usecase

import android.util.Log
import dev.tonholo.study.chatapp.data.remote.WebSocketProvider
import javax.inject.Inject

private const val TAG = "CloseConnectionUseCase"

class CloseConnectionUseCase @Inject constructor(
    private val webSocketProvider: WebSocketProvider,
) {
    operator fun invoke() {
        if (!webSocketProvider.isStarted) {
            Log.i(TAG, "invoke: trying to close an already closed connection. Skipping")
        } else {
            webSocketProvider.stopSocket()
        }
    }
}
