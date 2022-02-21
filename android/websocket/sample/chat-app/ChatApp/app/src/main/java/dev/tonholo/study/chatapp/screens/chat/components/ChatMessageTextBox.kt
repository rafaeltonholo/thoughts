package dev.tonholo.study.chatapp.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatMessageTextBox(
    modifier: Modifier = Modifier,
    onMessageSent: (message: String) -> Unit = {},
) {
    var message by remember { mutableStateOf("") }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        TextField(
            value = message,
            placeholder = {
                Text("Enter the message")
            },
            onValueChange = {
                message = it
            },
            modifier = Modifier
                .padding(end = 8.dp)
                .weight(1f),
        )
        Button(
            onClick = {
                onMessageSent(message)
                message = ""
            },
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = rememberVectorPainter(image = Icons.Default.Send),
                contentDescription = "Send message",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            )
        }
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
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(20.dp)
        ) {
            ChatMessageTextBox()
        }
    }
}
