package dev.tonholo.study.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tonholo.study.chatapp.data.dataStore.DataStoreManager
import dev.tonholo.study.chatapp.data.dataStore.extensions.getUsername
import dev.tonholo.study.chatapp.usecase.CloseConnectionUseCase
import dev.tonholo.study.chatapp.usecase.OpenConnectionUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val openConnectionUseCase: OpenConnectionUseCase,
    private val closeConnectionUseCase: CloseConnectionUseCase,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {

    fun openConnection() {
        viewModelScope.launch {
            dataStoreManager.getUsername {
                openConnectionUseCase(username = it)
            }
        }
    }

    fun closeConnection() {
        closeConnectionUseCase()
    }
}
