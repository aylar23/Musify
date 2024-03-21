package com.musify.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.musify.app.MainActivity
import com.musify.app.MainViewModel
import com.musify.app.navigation.screen.LoginNavGraph
import com.musify.app.presentation.NavGraphs
import com.musify.app.presentation.appCurrentDestinationAsState
import com.musify.app.presentation.destinations.ArtistScreenDestination
import com.musify.app.presentation.destinations.Destination
import com.musify.app.presentation.destinations.HomeScreenDestination
import com.musify.app.presentation.destinations.LoginScreenDestination
import com.musify.app.presentation.destinations.PlaylistScreenDestination
import com.musify.app.presentation.destinations.SettingsScreenDestination
import com.musify.app.presentation.login.LoginScreen
import com.musify.app.presentation.player.PlayerScreen
import com.musify.app.presentation.startAppDestination
import com.musify.app.ui.components.MiniPlayer
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationSpec
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel, playerBottomSheet: SheetState
) {

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    var showPlayer by remember {
        mutableStateOf(false)
    }

    val token by mainViewModel.token.collectAsState(initial = "")

    val playerController = mainViewModel.getPlayerController()

    val startRoute = if (token.isNotEmpty()) LoginScreenDestination else NavGraphs.root.startRoute
    val destination =
        navController.appCurrentDestinationAsState().value ?: startRoute.startAppDestination

    Scaffold(

        bottomBar = {
            if (destination.shouldShowScaffoldElements(token.isNotEmpty())) {
                Column {

                    mainViewModel.getPlayerController().selectedTrack?.let { song ->

                        MiniPlayer(
                            song = song,
                            playerController = mainViewModel.getPlayerController(),
                            onClick = {
                                scope.launch() {
                                    showPlayer = true
                                }
                            },
                            onPlayPauseClick = mainViewModel.getPlayerController()::onPlayPauseClick

                        )
                    }

                    BottomNavigationBar(navController = navController) { route ->
                    }

                }
            }

        }) { paddingValues ->

        if (token.isNotEmpty()) {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                startRoute = HomeScreenDestination,
            )
            if (showPlayer) {
                if (mainViewModel.getPlayerController().selectedTrack != null && showPlayer) {


                    PlayerScreen(
                        mainViewModel = mainViewModel,
                        playerController = mainViewModel.getPlayerController(),
                        playerBottomSheet = playerBottomSheet,

                        navigateToArtist = { artist ->
                            showPlayer = false
                            navController.navigate(ArtistScreenDestination(artist.id))
                        },
                        navigateToAlbum = { album ->
                            showPlayer = false
                            navController.navigate(PlaylistScreenDestination(album, MainActivity.ALBUMS))
                        },
                    ) {
                        showPlayer = false

                    }

                }
            }
        } else {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.login,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                startRoute = LoginScreenDestination,
            )
        }
    }

}

private fun Destination.shouldShowScaffoldElements(loggedIn: Boolean): Boolean {

    return loggedIn && this != SettingsScreenDestination
}

