package dev.tonholo.study.chatapp.screens.room

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.tonholo.study.chatapp.screens.room.components.RoomList
import dev.tonholo.study.chatapp.screens.room.components.RoomTopBar

@Composable
@ExperimentalMaterialApi
fun RoomScreen(
    viewModel: RoomViewModel = hiltViewModel(),
) {
    viewModel.subscribeTo("test")

    Scaffold(
        topBar = {
            RoomTopBar()
        }
    ) { padding ->
        val rooms = remember { viewModel.rooms }
        Column(modifier = Modifier.padding(padding)) {
            RoomList(rooms = rooms) {

            }
        }
    }
}
