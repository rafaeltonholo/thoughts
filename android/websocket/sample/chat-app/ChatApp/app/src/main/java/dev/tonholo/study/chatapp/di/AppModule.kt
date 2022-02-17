package dev.tonholo.study.chatapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.tonholo.study.chatapp.data.model.Owner
import dev.tonholo.study.chatapp.util.OwnerTypeAdapter
import okhttp3.OkHttpClient
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
    fun providesBaseWebSocketUrl(): String = "ws://192.168.1.21:8080/chat"

    @Provides
    @Singleton
    fun providesGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(Owner::class.java, OwnerTypeAdapter())
            .create()
}

@Qualifier
annotation class BaseWsUrl
