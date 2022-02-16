package dev.tonholo.study.chatapp.screens.chat.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatTopBar(
    room: String,
    onBackButtonClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackButtonClicked) {
            Image(
                painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                contentDescription = "Back button",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
            )
        }
        Text(
            text = "Welcome to $room!",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.onPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
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
        ChatTopBar(room = "Testing room") {

        }
    }
}
