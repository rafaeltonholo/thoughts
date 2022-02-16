package dev.tonholo.data

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val owner: Connection,
    val text: String = "",
    val rooms: List<String> = listOf(),
)
