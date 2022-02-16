package dev.tonholo.data

sealed class Room {
    object All : Room() {
        override fun toString(): String = "All"
    }
    data class PrivateRoom(
        val name: String,
    ): Room() {
        override fun toString(): String = name
    }
}

fun String.toRoom() = when(this) {
    "all" -> Room.All
    else -> Room.PrivateRoom(name = this)
}
