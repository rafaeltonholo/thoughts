package dev.tonholo.study.chatapp.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.tonholo.study.chatapp.screens.chat.components.ChatMessageList
import dev.tonholo.study.chatapp.screens.chat.components.ChatMessageTextBox
import dev.tonholo.study.chatapp.screens.chat.components.ChatTopBar

@Composable
@Destination
fun ChatScreen(
    room: String,
    navigator: DestinationsNavigator,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    viewModel.enterRoom(room)
    viewModel.listenToMessages()
    val messages = remember { viewModel.messages }
    val hasNewMessage by remember { viewModel.hasNewMessage }
    val error by remember { viewModel.error }

    Scaffold(
        topBar = {
            ChatTopBar(room = room) {
                navigator.popBackStack()
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            ChatMessageList(
                messages = messages,
                modifier = Modifier.weight(1f),
                hasNewMessage = hasNewMessage,
            )
            ChatMessageTextBox(
                modifier = Modifier
                    .padding(8.dp),
                onMessageSent = { message ->
                    viewModel.sendMessage(message)
                }
            )
        }
    }
}
