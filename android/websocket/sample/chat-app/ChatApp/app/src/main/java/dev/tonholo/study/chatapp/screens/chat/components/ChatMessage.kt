package dev.tonholo.study.chatapp.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.ui.shape.ChatBoxShape
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatMessage(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .background(MaterialTheme.colors.secondary, ChatBoxShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary,
        )
    }
}

@Preview
@Composable
private fun LightThemePreview() = Preview(false)

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
private fun DarkThemePreview() = Preview(true)

@Composable
private fun Preview(darkMode: Boolean) {
    ChatAppTheme(darkTheme = darkMode) {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            ChatMessage("Hello World")
            ChatMessage("Hello World 1")
            ChatMessage("Hello World 2")
            ChatMessage("Hello World 3")
            ChatMessage("Hello World 4")
            ChatMessage("Hello World 5")
            ChatMessage("Hello World 6")
        }
    }
}
