package dev.tonholo.study.chatapp.data.model

import java.util.*

data class Message(
    val owner: Owner,
    val text: String = "",
    val rooms: List<NamedRoom>? = listOf(),
    var received: Date,
)
