package dev.tonholo.study.chatapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.tonholo.study.chatapp.data.ChatAppWebSocketListener
import okhttp3.WebSocketListener

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {
    @Binds
    fun bindsWebSocketListener(chatAppWebSocketListener: ChatAppWebSocketListener): WebSocketListener
}
