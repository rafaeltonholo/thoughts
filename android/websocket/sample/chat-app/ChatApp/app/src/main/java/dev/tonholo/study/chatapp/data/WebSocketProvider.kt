package dev.tonholo.study.chatapp.data

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.tonholo.study.chatapp.di.BaseWsUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

private const val ASSISTED_ROOM = "room"
private const val ASSISTED_USERNAME = "username"

@AssistedFactory
interface WebSocketProviderFactory {
    fun create(
        @Assisted(ASSISTED_ROOM) room: String,
        @Assisted(ASSISTED_USERNAME) username: String,
    ): WebSocketProvider
}

const val NORMAL_CLOSURE_STATUS = 1000

class WebSocketProvider @AssistedInject constructor(
    @Assisted(ASSISTED_ROOM) private val room: String,
    @Assisted(ASSISTED_USERNAME) private val username: String,
    private val client: OkHttpClient,
    @BaseWsUrl private val baseWsUrl: String,
    private val webSocketListener: WebSocketListener,
) {
    private lateinit var webSocket: WebSocket

    fun startSocket() {
        val request = Request.Builder()
            .url("$baseWsUrl/$room/$username")
            .build()

        webSocket = client.newWebSocket(
            request = request,
            webSocketListener,
        )

        client.dispatcher.executorService.shutdown()
    }

    fun sendMessage(socketEvent: SocketEvent.Message) {
        when(socketEvent) {
            is SocketEvent.Message.ByteStringMessage ->
                webSocket.send(socketEvent.byteString)
            is SocketEvent.Message.TextMessage ->
                webSocket.send(socketEvent.text)
        }
    }

    fun stopSocket() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }
}
