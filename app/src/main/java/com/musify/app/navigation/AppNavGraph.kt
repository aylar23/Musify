package com.musify.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column

import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.MainViewModel
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.navigation.screen.Screen
import com.musify.app.presentation.player.PlayerScreen
import com.musify.app.presentation.settings.SettingsScreen
import com.musify.app.ui.components.MiniPlayer
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel,
    playerBottomSheet: SheetState
) {

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    var showPlayer by remember {
        mutableStateOf(false)
    }


    val playerController = mainViewModel.getPlayerController()
    Scaffold(

        bottomBar = {
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
        }) { paddingValues ->


        NavHost(
            navController = navController,
            startDestination = NavScreen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { fadeIn(animationSpec = tween(100)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) },
        ) {

            composable(NavScreen.Home.route) {
                HomeNavGraph(paddingValues, navController)
            }

            composable(NavScreen.Search.route) {
                SearchNavGraph(paddingValues, navController)
            }

            composable(NavScreen.MyPlaylists.route) {
                LibraryNavGraph(paddingValues, navController)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    settingsViewModel = hiltViewModel()
                )
            }


        }


        if (mainViewModel.getPlayerController().selectedTrack != null && showPlayer) {



                PlayerScreen(
                    mainViewModel = mainViewModel,
                    playerController = mainViewModel.getPlayerController(),
                    playerBottomSheet = playerBottomSheet,

                    navigateToArtist = { artist ->
                        scope.launch {
                            showPlayer = false
//                                    currentInnerNavController?.navigate(Screen.Artist.route + "/${artist.id}")
                        }
                    },
                    navigateToAlbum = { album ->
                        scope.launch {
                            showPlayer = false
//                                    currentInnerNavController?.navigate(Screen.Playlist.route + "/$ALBUMS/${album}")

                        }
                    },
                ) {
                    showPlayer = false

                }

        }

    }

}

