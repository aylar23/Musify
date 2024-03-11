package com.musify.app.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding

import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
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
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetState
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch
import java.lang.Math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    mainViewModel: MainViewModel,
    playerBottomSheet: FlexibleSheetState
) {

    val navController = rememberNavController()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    val scope = rememberCoroutineScope()

    var showPlayer by remember {
        mutableStateOf(false)
    }

    val currentDestination = navController.currentDestination
    val bottomSheetState = rememberModalBottomSheetState(true)

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
                                playerBottomSheet.fullyExpand()
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


            FlexibleBottomSheet(onDismissRequest = { showPlayer = false },
                sheetState = playerBottomSheet,
                dragHandle = {}
            ) {
                PlayerScreen(
                    mainViewModel = mainViewModel,
                    playerController = mainViewModel.getPlayerController(),
                    playerBottomSheet = playerBottomSheet,

                    navigateToArtist = { artist ->
                        scope.launch {
                            playerBottomSheet.hide()
                            showPlayer = false
//                                    currentInnerNavController?.navigate(Screen.Artist.route + "/${artist.id}")
                        }
                    },
                    navigateToAlbum = { album ->
                        scope.launch {
                            playerBottomSheet.hide()
                            showPlayer = false
//                                    currentInnerNavController?.navigate(Screen.Playlist.route + "/$ALBUMS/${album}")

                        }
                    },
                ) {

                    scope.launch {
                        playerBottomSheet.hide()
                    }

                }
            }
        }


    }

}

class BottomSheetNestedScrollInterceptor : NestedScrollConnection {
    private var arrivedBoundarySource: NestedScrollSource? = null

    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // Reset the state variable
        if (source == NestedScrollSource.Drag && arrivedBoundarySource == NestedScrollSource.Fling) {
            arrivedBoundarySource = null
        }

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // The sub-layout can't consume completely,
        // which means that the boundary has been reached.
        if (arrivedBoundarySource == null && abs(available.y) > 0) {
            arrivedBoundarySource = source
        }

        // Decide whether to consume according to the sub-layout
        // consumption when reaching the boundary.
        if (arrivedBoundarySource == NestedScrollSource.Fling) {
            return available
        }

        return Offset.Zero
    }

    override suspend fun onPostFling(
        consumed: Velocity,
        available: Velocity
    ): Velocity {
        arrivedBoundarySource = null
        return super.onPostFling(consumed, available)
    }
}

@Composable
fun rememberBottomSheetNestedScrollInterceptor(): BottomSheetNestedScrollInterceptor {
    return remember { BottomSheetNestedScrollInterceptor() }
}
