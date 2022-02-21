package dev.tonholo.study.chatapp.data.remote

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

class ChatAppWebSocketListener @Inject constructor(
    private val socketEventHandler: SocketEventHandler,
) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        socketEventHandler.onOpen(response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        socketEventHandler.onMessage(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        socketEventHandler.onMessage(bytes)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        socketEventHandler.onClosing()
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        socketEventHandler.onFailure(t)
    }
}
