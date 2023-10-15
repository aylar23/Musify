package com.musify.app

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.musify.app.navigation.AppNavGraph
import com.musify.app.player.PlaybackService
import com.musify.app.presentation.player.PlayerScreen
import com.musify.app.ui.theme.MusifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        var controller: MediaController
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()
                // call playback command methods on the controller like `controller.play()`
            },
            MoreExecutors.directExecutor()
        )


        setContent {
            MusifyTheme {
                // A surface container using the 'background' color from the theme
                val mainViewModel = hiltViewModel<MainViewModel>()
                val playerBottomSheet = rememberStandardBottomSheetState(
                    initialValue = SheetValue.Hidden,
                    skipHiddenState = false
                )
                val scaffoldState = rememberBottomSheetScaffoldState(playerBottomSheet)
                val scope = rememberCoroutineScope()
                BottomSheetScaffold(scaffoldState = scaffoldState,
                    sheetDragHandle = {},
                    sheetPeekHeight = 0.dp,
                    sheetContent = {

                        PlayerScreen(
                            playerController = mainViewModel.getPlayerController(),
                            playerBottomSheet = playerBottomSheet
                        ) {

                            scope.launch {
                                playerBottomSheet.hide()
                            }

                        }

                    },
                    content = {
                        AppNavGraph(
                            mainViewModel = mainViewModel,
                            playerBottomSheet = playerBottomSheet
                        )
                    })

            }

        }
    }


    companion object {

        val TOPS = "tops"
        val PLAYLISTS = "playlists"
        val ALBUMS = "albums"

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusifyTheme {
        Greeting("Android")
    }
}