package dev.tonholo.study.chatapp.di.coroutine

import javax.inject.Qualifier

@Qualifier
annotation class DefaultDispatcher

@Qualifier
annotation class IODispatcher

@Qualifier
annotation class MainDispatcher

@Qualifier
annotation class MainImmediateDispatcher

@Qualifier
annotation class UnconfinedDispatcher
