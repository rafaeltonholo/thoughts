package dev.tonholo.study.chatapp.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.data.model.Message
import dev.tonholo.study.chatapp.data.model.Owner
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatMessageList(
    messages: List<Message>,
    modifier: Modifier = Modifier,
) {
    if (messages.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.padding(8.dp)
        ) {
            items(messages) { message ->
                with(message) {
                    Column(
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                    ) {
                        ChatMessage(text = text)
                        Text(
                            text = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).run {
                                format(received)
                            },
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.75f),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp),
                        )
                    }
                }
            }
        }
    } else {
        Text(
            text = "No messages received. Waiting for messages...",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
            modifier = modifier.fillMaxSize(),
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
        val messages = (0..100).map { Message(owner = Owner.Unknown, text = "Message $it", received = Date()) }
        Column(
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            ChatMessageList(messages)
        }
    }
}
