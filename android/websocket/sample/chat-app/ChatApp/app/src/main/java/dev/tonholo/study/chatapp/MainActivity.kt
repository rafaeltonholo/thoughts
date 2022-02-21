package dev.tonholo.study.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dev.tonholo.study.chatapp.screens.NavGraphs
import dev.tonholo.study.chatapp.screens.chat.ChatScreen
import dev.tonholo.study.chatapp.screens.room.RoomScreen
import dev.tonholo.study.chatapp.ui.theme.ChatAppTheme

@AndroidEntryPoint
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.openConnection()

        setContent {
            ChatAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }

    override fun onDestroy() {
        viewModel.closeConnection()
        super.onDestroy()
    }
}
