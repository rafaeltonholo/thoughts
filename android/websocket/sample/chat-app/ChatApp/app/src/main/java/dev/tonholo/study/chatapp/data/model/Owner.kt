package dev.tonholo.study.chatapp.data.model

sealed class Owner(
    val id: Int,
    val name: String,
) {
    object Unknown: Owner(-2, "unknown user")

    object Server: Owner(-1, "Broadcast Server")

    class User(
        id: Int,
        name: String,
    ): Owner(id, name)
}
