package dev.tonholo.study.chatapp.screens.room.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.data.model.NamedRoom
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@Composable
@ExperimentalMaterialApi
fun RoomCard(
    namedRoom: NamedRoom,
    modifier: Modifier = Modifier,
    onClicked: (NamedRoom) -> Unit = {},
) = with(namedRoom) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        onClick = {
            onClicked(namedRoom)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
            )
            Text(
                text = "$onlineMembers online member(s)",
                style = MaterialTheme.typography.subtitle2,
            )
        }
    }
}

@Preview
@Composable
@ExperimentalMaterialApi
private fun LightThemePreview() = Preview(false)

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_MASK,
)
@Composable
@ExperimentalMaterialApi
private fun DarkThemePreview() = Preview(true)

@ExperimentalMaterialApi
@Composable
private fun Preview(darkMode: Boolean) {
    ChatAppTheme(darkTheme = darkMode) {
        RoomCard(
            NamedRoom(
                name = "Test",
                onlineMembers = 10,
            )
        )
    }
}
