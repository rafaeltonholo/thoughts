package dev.tonholo.study.chatapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tonholo.study.chatapp.data.ChatAppWebSocketListener
import dev.tonholo.study.chatapp.di.coroutine.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWebSocketFactory(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(39, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ -> true } // Should be validated on production apps. As this is a sample, don't need.
        .build()


    @Provides
    @Singleton
    @BaseWsUrl
    fun providesBaseWebSocketUrl(): String = "ws://192.168.1.21:8080/ws/chat"

}

@Qualifier
annotation class BaseWsUrl
