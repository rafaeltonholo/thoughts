package dev.tonholo.study.chatapp.screens.room.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
fun RequestIdentificationDialog(
    receivedError: String? = null,
    onDismiss: () -> Unit = {},
    onSaveUser: (String) -> Unit = {},
) {
    var username by remember { mutableStateOf("") }
    var error = receivedError

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            DialogTitle()
        },
        text = {
            DialogContent(username, error) {
                error = if (it.isNotBlank()) {
                    null
                } else {
                    receivedError
                }

                username = it
            }
        },
        confirmButton = {
            DialogButtons {
                onSaveUser(username)
            }
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
private fun DialogTitle() =
    Text(
        "Identify yourself",
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
    )

@Composable
private fun DialogContent(
    username: String = "",
    error: String? = null,
    onValueChange: (String) -> Unit = {},
) {
    Column {
        Text(
            text = "To chat with someone you need to identify yourself.",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(end = 8.dp),
        )
        OutlinedTextField(
            value = username,
            label = {
                Text(text = "Username")
            },
            onValueChange = onValueChange,
            isError = !error.isNullOrEmpty(),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun DialogButtons(
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = (-8).dp),
    ) {
        TextButton(
            onClick = onClick
        ) {
            Text(
                text = "SAVE",
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
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.surface, MaterialTheme.shapes.medium)
                .padding(20.dp),
            contentAlignment = Alignment.Center,
        ) {
            var username by remember { mutableStateOf("") }
            Column {
                DialogTitle()
                DialogContent(username) {
                    username = it
                }
                DialogButtons()
            }
        }
    }
}
