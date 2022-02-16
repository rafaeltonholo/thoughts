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
import dev.tonholo.study.chatapp.screens.chat.components.ChatMessageList
import dev.tonholo.study.chatapp.screens.chat.components.ChatMessageTextBox
import dev.tonholo.study.chatapp.screens.chat.components.ChatTopBar

@Composable
fun ChatScreen(
    room: String = "firstTest",
    username: String = "Rafael",
    viewModel: ChatViewModel = hiltViewModel(),
) {
    viewModel.subscribeTo(room, username)
    val messages = remember { viewModel.messages }
    val error by remember { viewModel.error }

    Scaffold(
        topBar = {
            ChatTopBar(room = room) {

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
                modifier = Modifier.weight(1f)
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
