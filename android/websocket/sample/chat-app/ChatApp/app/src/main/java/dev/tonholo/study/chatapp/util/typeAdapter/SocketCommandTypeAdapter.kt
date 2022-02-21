package dev.tonholo.study.chatapp.util.typeAdapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dev.tonholo.study.chatapp.data.model.SocketCommand

private const val BASE_TYPE = "dev.tonholo.data.Command"
private const val ENTER_ROOM_TYPE = "$BASE_TYPE.EnterRoom"
private const val LEAVE_ROOM_TYPE = "$BASE_TYPE.LeaveRoom"
private const val SEND_MESSAGE_TYPE = "$BASE_TYPE.SendMessage"

class SocketCommandTypeAdapter : TypeAdapter<SocketCommand>() {
    override fun write(out: JsonWriter, value: SocketCommand): Unit = with(out) {
        beginObject()
        name("type")

        when (value) {
            is SocketCommand.Connect -> throw NotImplementedError("Not supported")
            is SocketCommand.EnterRoom -> {
                value(ENTER_ROOM_TYPE)
                name(value::roomName.name)
                value(value.roomName)
            }
            SocketCommand.LeaveRoom -> {
                value(LEAVE_ROOM_TYPE)
            }
            is SocketCommand.SendMessage -> {
                value(SEND_MESSAGE_TYPE)
                name(value::message.name)
                value(value.message)
            }
        }

        endObject()
    }

    override fun read(`in`: JsonReader?): SocketCommand {
        throw NotImplementedError("Not going to receive SocketCommand from Server")
    }
}
