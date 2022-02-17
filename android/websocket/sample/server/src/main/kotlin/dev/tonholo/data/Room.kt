package dev.tonholo.data

import kotlinx.serialization.Serializable

@Serializable
sealed class Room {
    object All : Room() {
        override fun toString(): String = "All"
    }

    @Serializable
    data class NamedRoom(
        val name: String,
        var onlineMembers: Int,
    ) : Room() {
        override fun toString(): String = name

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as NamedRoom
            return name == other.name
        }

        override fun hashCode(): Int = name.hashCode()
    }
}

fun String.toRoom() = when (this) {
    "all" -> Room.All
    else -> Room.NamedRoom(name = this, onlineMembers = 1)
}
