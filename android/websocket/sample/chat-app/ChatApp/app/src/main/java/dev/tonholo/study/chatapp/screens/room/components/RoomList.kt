package dev.tonholo.study.chatapp.screens.room.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.tonholo.study.chatapp.data.model.NamedRoom
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@ExperimentalMaterialApi
@Composable
fun RoomList(
    rooms: List<NamedRoom>,
    onRoomClicked: (NamedRoom) -> Unit = {},
) {
    val rows = 3
    val chunkedList = rooms.chunked(rows)
    LazyColumn(
        modifier = Modifier
            .padding(8.dp),
    ) {
        itemsIndexed(chunkedList) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            Row {
                val modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
                    .padding(8.dp)
                item.forEach { item ->
                    RoomCard(
                        namedRoom = item,
                        modifier = modifier,
                        onClicked = onRoomClicked
                    )
                }

                repeat(rows - item.size) {
                    Box(modifier = modifier)
                }
            }
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

@Composable
@ExperimentalMaterialApi
private fun Preview(darkMode: Boolean) {
    ChatAppTheme(darkTheme = darkMode) {
        Surface(color = MaterialTheme.colors.background) {
            val rooms = (1 until 100).map {
                NamedRoom(
                    name = "Test $it",
                    onlineMembers = it,
                )
            }

            RoomList(rooms = rooms)
        }
    }
}
