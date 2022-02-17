package dev.tonholo.socket

import dev.tonholo.data.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
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
        private val rooms = Collections.synchronizedCollection<Room.NamedRoom>(LinkedHashSet())

        internal val instance by lazy {
            ServerSessionImpl()
        }
    }

    override suspend fun onOpen(session: WebSocketServerSession) {
        println("Adding user!")
        val username = session.call.parameters.getOrFail("username")

        if (connections.values.any { it.name.lowercase() == username.lowercase() }) {
            println("shutting down user with same name of a connected user")
            session.cancel("There is already a user connected with the name $username")
            return
        }

        val currentConnection = Connection.User(
            session = session,
            name = username,
            currentRoom = Room.All,
        )

        connections += session to currentConnection
        broadcastRoomList()
    }

    override suspend fun handleMessages(session: WebSocketServerSession) {
        with(session) {
            if (!session.isActive) return

            connections[session]?.let { currentConnection ->

                try {
                    sendGreetingMessage(this, currentConnection)

                    incoming.consumeAsFlow()
                        .filterIsInstance<Frame.Text>()
                        .map { it.readText() }
                        .map { Json.decodeFromString<Command>(it) }
                        .collect { command ->
                            when (command) {
                                is Command.Connect -> handleCommandConnect(currentConnection, command)
                                is Command.EnterRoom -> handleCommandEnterRoom(currentConnection, command)
                                Command.LeaveRoom -> handleCommandLeaveRoom(currentConnection)
                                is Command.SendMessage -> handleCommandSendMessage(currentConnection, command)
                            }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    println("Removing ${currentConnection.name}!")
                    val oldConnection = connections.remove(session)
                    oldConnection?.let { con ->
                        val room = con.currentRoom
                        connections.values
                            .filter { it.currentRoom == room }
                            .forEach {
                                it.session?.send(
                                    Json.encodeToString(
                                        Message(
                                            owner = Connection.Server,
                                            text = "${con.name} disconnected from the server!",
                                        )
                                    )
                                )
                            }

                        removeUserFromRoom(con, room)
                    }
                }
            }
        }
    }

    private suspend fun sendGreetingMessage(session: WebSocketServerSession, connection: Connection.User) {
        val room = connection.currentRoom
        session.send(
            Json.encodeToString(
                Message(
                    owner = Connection.Server,
                    text = "You are connected! There are ${
                        connections.count { it.value.currentRoom == room }
                    } users here.",
                    rooms = if (room == Room.All) {
                        rooms.toList()
                    } else {
                        listOf()
                    },
                )
            )
        )
    }

    private suspend fun removeUserFromRoom(connection: Connection.User, room: Room) {
        println("Removing user (${connection.name}) from $room")
        if (room is Room.NamedRoom) {
            with(rooms.first { it == room }) {
                if (--onlineMembers == 0) {
                    rooms.remove(this)
                }
            }

            broadcastRoomList()
        }
    }

    private suspend fun broadcastRoomList() {
        connections.values
            .filter { connection -> connection.currentRoom == Room.All }
            .forEach { connection ->
                connection.session?.run {
                    val message = Message(
                        owner = Connection.Server,
                        text = "Here is a list of open rooms",
                        rooms = rooms
                            .map { it as Room }
                            .toList(),
                    )
                    send(
                        Json.encodeToString(message)
                    )
                }
            }
    }

    private fun handleCommandConnect(connection: Connection.User, command: Command.Connect) {}

    private suspend fun handleCommandEnterRoom(
        connection: Connection.User,
        command: Command.EnterRoom,
    ) = with(command) {
        println("${connection.name} entering in $roomName")
        val room = roomName.toRoom() as Room.NamedRoom
        connection.currentRoom = room

        rooms.firstOrNull { it == room }?.let {
            it.onlineMembers++
        } ?: run {
            rooms += room
        }

        connections.values
            .filter { it.currentRoom == room }
            .forEach {
                it.session?.send(
                    Json.encodeToString(
                        Message(
                            owner = Connection.Server,
                            text = "${connection.name} connected to $room!",
                        )
                    )
                )
            }

        broadcastRoomList()
    }

    private suspend fun handleCommandLeaveRoom(connection: Connection.User) {
        println("${connection.name} leaving in ${connection.currentRoom}")
        val oldRoom = connection.currentRoom
        connection.currentRoom = Room.All
        removeUserFromRoom(connection, oldRoom)
    }

    private suspend fun handleCommandSendMessage(connection: Connection.User, command: Command.SendMessage) {
        connections.values
            .filter {
                it.currentRoom == connection.currentRoom
            }
            .forEach {
                it.session?.send(
                    Json.encodeToString(
                        Message(
                            owner = connection,
                            text = command.message,
                        )
                    )
                )
            }
    }
}

object ServerSession : ServerSessionContract by ServerSessionImpl.instance
