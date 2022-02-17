package dev.tonholo.study.chatapp.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import dev.tonholo.study.chatapp.data.model.Owner

class OwnerTypeAdapter : TypeAdapter<Owner>() {
    override fun write(out: JsonWriter, value: Owner): Unit = with(out) {
        beginObject()
        name("type")
        value(
            when (value) {
                Owner.Server -> "dev.tonholo.data.Connection.Server"
                Owner.Unknown -> "dev.tonholo.data.Connection.Server"
                is Owner.User -> "dev.tonholo.data.Connection.User"
            }
        )
        name("id")
        value(value.id)
        name("name")
        value(value.name)
        endObject()
    }

    override fun read(`in`: JsonReader): Owner = with(`in`) {
        beginObject()
        var fieldName = ""
        var type = ""
        var name = ""
        var id = -1

        while (hasNext()) {
            val token = peek()

            if (token == JsonToken.NAME) {
                fieldName = nextName()
            }

            peek()
            when (fieldName) {
                "type" -> type = nextString()
                "name" -> name = nextString()
                "id" -> id = nextInt()
            }
        }

        endObject()
        return when {
            "Connection.Server" in type -> Owner.Server
            "Connection.User" in type -> Owner.User(
                id,
                name,
            )
            else -> Owner.Unknown
        }

    }

}
