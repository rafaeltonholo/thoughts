package dev.tonholo.study.chatapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManager
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManagerImpl
import dev.tonholo.study.chatapp.data.remote.ChatAppWebSocketListener
import dev.tonholo.study.chatapp.data.model.Owner
import dev.tonholo.study.chatapp.data.model.SocketCommand
import dev.tonholo.study.chatapp.util.typeAdapter.OwnerTypeAdapter
import dev.tonholo.study.chatapp.util.typeAdapter.SocketCommandTypeAdapter
import okhttp3.OkHttpClient
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindsWebSocketListener(chatAppWebSocketListener: ChatAppWebSocketListener): WebSocketListener

    companion object {
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
        fun providesBaseWebSocketUrl(): String = "ws://192.168.1.21:8080/chat"

        @Provides
        @Singleton
        fun providesGson(): Gson =
            GsonBuilder()
                .registerTypeAdapter(Owner::class.java, OwnerTypeAdapter())
                .registerTypeAdapter(SocketCommand::class.java, SocketCommandTypeAdapter())
                .create()

        @Provides
        @Singleton
        fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
            DataStoreManagerImpl(context)
    }
}

@Qualifier
annotation class BaseWsUrl
