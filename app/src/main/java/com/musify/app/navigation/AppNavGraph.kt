package com.musify.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.MainViewModel
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.navigation.screen.Screen
import com.musify.app.presentation.settings.SettingsScreen
import com.musify.app.ui.components.MiniPlayer
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel, playerBottomSheet: SheetState
) {

    val navController = rememberNavController()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)


    val scope = rememberCoroutineScope()

    Scaffold(

        bottomBar = {
            Column {

                mainViewModel.getPlayerController().selectedTrack?.let { song ->
                    HorizontalDivider()

                    MiniPlayer(
                        song = song,
                        onClick = { scope.launch() { playerBottomSheet.expand() } },
                        onPlayPauseClick = mainViewModel.getPlayerController()::onPlayPauseClick

                    )
                }


//                HorizontalDivider()
                BottomNavigationBar(navController = navController)

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


//        if (playerBottomSheet.isVisible){
//            PlayerScreen(
//                playerController = mainViewModel.getPlayerController(),
//                playerBottomSheet
//            ) {
//                scope.launch { playerBottomSheet.hide() }
//
//            }
//        }


    }

}


