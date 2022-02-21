package dev.tonholo.study.chatapp.screens.room

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.tonholo.study.chatapp.screens.destinations.ChatScreenDestination
import dev.tonholo.study.chatapp.screens.room.components.RequestIdentificationDialog
import dev.tonholo.study.chatapp.screens.room.components.RoomList
import dev.tonholo.study.chatapp.screens.room.components.RoomTopBar

@Destination(
    start = true,
)
@Composable
@ExperimentalMaterialApi
fun RoomScreen(
    navigator: DestinationsNavigator,
    viewModel: RoomViewModel = hiltViewModel(),
) {
    val username by remember { viewModel.usernameState }
    val requestUsername by remember { viewModel.requestUsername }
    val error by remember { viewModel.error }

    LaunchedEffect(Unit) {
        viewModel.listenRooms()
    }

    if (requestUsername) {
        RequestIdentificationDialog(
            receivedError = error,
            onDismiss = { viewModel.listenRooms() },
            onSaveUser = { viewModel.saveUsername(it) },
        )
    }

    Scaffold(
        topBar = {
            RoomTopBar(username) {
                navigator.popBackStack()
            }
        }
    ) { padding ->
        val rooms = remember { viewModel.rooms }

        Column(modifier = Modifier.padding(padding)) {
            if (rooms.isNotEmpty()) {
                RoomList(rooms = rooms) {
                    navigator.navigate(ChatScreenDestination(
                        room = it.name,
                    ))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "There is no opened rooms.",
                        style = MaterialTheme.typography.h4,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
