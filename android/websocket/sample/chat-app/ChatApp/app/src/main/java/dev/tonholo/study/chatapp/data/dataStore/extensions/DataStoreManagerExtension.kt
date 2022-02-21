package dev.tonholo.study.chatapp.data.dataStore.extensions

import androidx.datastore.preferences.core.stringPreferencesKey
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManager

private const val KEY_NAME = "username"

suspend fun DataStoreManager.setUsername(value: String) {
    val key = stringPreferencesKey(KEY_NAME)
    store(key, value)
}

suspend fun DataStoreManager.getUsername(onReadFinished: (String) -> Unit) {
    val key = stringPreferencesKey(KEY_NAME)
    read(key) {
        onReadFinished(this)
    }
}
