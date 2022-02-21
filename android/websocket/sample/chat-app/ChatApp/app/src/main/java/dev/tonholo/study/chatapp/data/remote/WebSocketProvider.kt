package dev.tonholo.study.chatapp.data.remote

import com.google.gson.Gson
import dev.tonholo.study.chatapp.data.model.SocketCommand
import dev.tonholo.study.chatapp.di.BaseWsUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

const val NORMAL_CLOSURE_STATUS = 1000

@Singleton
class WebSocketProvider @Inject constructor(
    private val client: OkHttpClient,
    @BaseWsUrl private val baseWsUrl: String,
    private val webSocketListener: WebSocketListener,
    private val gson: Gson,
) {
    private lateinit var webSocket: WebSocket
    var isStarted = false
        private set

    fun startSocket(username: String) {
        val request = Request.Builder()
            .url("$baseWsUrl/$username")
            .build()

        webSocket = client.newWebSocket(
            request = request,
            webSocketListener,
        )

        client.dispatcher.executorService.shutdown() // This is necessary to releasing resources.
        isStarted = true
    }

    fun sendCommand(command: SocketCommand) {
        webSocket.send(gson.toJson(command))
    }

    fun stopSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        isStarted = false
    }
}
