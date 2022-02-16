package dev.tonholo.socket

import dev.tonholo.data.Connection
import dev.tonholo.data.Message
import dev.tonholo.data.Room
import dev.tonholo.data.toRoom
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

private interface ServerSessionContract {
    suspend fun onOpen(session: WebSocketServerSession)
    suspend fun handleMessages(session: WebSocketServerSession)
}

private class ServerSessionImpl : ServerSessionContract {
    companion object {
        private val connections = Collections.synchronizedMap<WebSocketServerSession, Connection.User?>(LinkedHashMap())
        private val rooms = Collections.synchronizedMap<Room.PrivateRoom, Int>(LinkedHashMap())

        internal val instance by lazy {
            ServerSessionImpl()
        }
    }

    override suspend fun onOpen(session: WebSocketServerSession) {
        println("Adding user!")
        val room = session.call.parameters.getOrFail("room").toRoom()
        val username = session.call.parameters.getOrFail("username")

        if (connections.values.any { it.name.lowercase() == username.lowercase() }) {
            session.cancel("There is already a user connected with the name $username")
            return
        }

        val currentConnection = Connection.User(
            session = session,
            name = username,
            currentRoom = room,
        )

        (room as? Room.PrivateRoom)?.let {
            val count = if (!rooms.contains(it)) {
                1
            } else {
                rooms[it]?.plus(1)
            }

            rooms[it] = count
        }

        connections.values
            .filter { it.currentRoom == room }
            .forEach {
                it.session?.send(
                    Json.encodeToString(
                        Message(
                            owner = Connection.Server,
                            text = "${currentConnection.name} connected to $room!",
                        )
                    )
                )
            }

        connections += session to currentConnection
        broadcastRoomList()
    }

    override suspend fun handleMessages(session: WebSocketServerSession) {
        with(session) {
            if (!session.isActive) return

            connections[session]?.let { currentConnection ->
                val room = session.call.parameters.getOrFail("room").toRoom()

                try {
                    send(
                        Json.encodeToString(
                            Message(
                                owner = Connection.Server,
                                text = "You are connected! There are ${
                                    connections.count { it.value.currentRoom == room }
                                } users here.",
                            )
                        )
                    )

                    incoming.consumeAsFlow()
                        .mapNotNull { it as? Frame.Text }
                        .map { it.readText() }
                        .collect { receivedMessage ->
                            connections.values
                                .filter {
                                    it.currentRoom == currentConnection.currentRoom
                                }
                                .forEach {
                                    it.session?.send(
                                        Json.encodeToString(
                                            Message(
                                                owner = currentConnection,
                                                text = receivedMessage,
                                            )
                                        )
                                    )
                                }
                        }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                } finally {
                    println("Removing $currentConnection!")
                    connections.remove(session)
                    connections.values
                        .filter { it.currentRoom == room }
                        .forEach {
                            it.session?.send(
                                Json.encodeToString(
                                    Message(
                                        owner = Connection.Server,
                                        text = "${currentConnection.name} disconnected from $room!",
                                    )
                                )
                            )
                        }

                    if (room is Room.PrivateRoom) {
                        val roomCount = rooms[room]?.minus(1) ?: 0
                        if (roomCount == 0) {
                            rooms.remove(room)
                        } else {
                            rooms[room] = roomCount
                        }
                        broadcastRoomList()
                    }
                }
            }
        }
    }

    private suspend fun broadcastRoomList() {
        connections.values
            .filter { connection -> connection.currentRoom == Room.All }
            .forEach { connection ->
                connection.session?.run {
                    send(
                        Json.encodeToString(
                            Message(
                                owner = Connection.Server,
                                text = "Here is a list of open rooms",
                                rooms = rooms
                                    .map { "${it.key.name} (${it.value} online members)" }
                                    .toList(),
                            )
                        )
                    )
                }
            }
    }
}

object ServerSession : ServerSessionContract by ServerSessionImpl.instance
