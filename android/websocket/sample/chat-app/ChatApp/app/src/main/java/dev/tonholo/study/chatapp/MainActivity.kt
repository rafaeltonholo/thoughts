package dev.tonholo.study.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.tonholo.study.chatapp.screens.chat.ChatScreen
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                ChatScreen()
            }
        }
    }
}
