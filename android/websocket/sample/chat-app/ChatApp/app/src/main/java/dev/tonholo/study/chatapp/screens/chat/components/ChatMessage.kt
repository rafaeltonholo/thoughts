package dev.tonholo.study.chatapp.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.ui.shape.ChatBoxShape
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatMessage(
    text: String,
    owner: String,
    ownedByUser: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(8.dp)
            .background(
                if (ownedByUser) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.secondary
                },
                ChatBoxShape(8.dp, ownedByUser),
            )
            .padding(16.dp)
    ) {
        Text(
            text = owner,
            modifier = Modifier
                .offset(y = (-24).dp)
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .align(if (ownedByUser) {
                    Alignment.TopStart
                } else {
                    Alignment.TopEnd
                })
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.subtitle2,
            color = Color.Black,
        )
        Text(
            text = text,
            color = if (ownedByUser) {
                MaterialTheme.colors.onPrimary
            } else {
                MaterialTheme.colors.onSecondary
            },
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
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            ChatMessage("Hello World", "Rafael")
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 1", "Jessy", false)
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 2", "Rafael")
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 3", "Rafael")
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 4", "James", false)
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 5", "Ash", false)
            Spacer(modifier = Modifier.height(8.dp))
            ChatMessage("Hello World 6", "Rafael")
        }
    }
}
