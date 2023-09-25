package com.musify.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.domain.models.defaultSong
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.presentation.myplaylist.MyPlaylistsScreen
import com.musify.app.presentation.player.PlayerScreen
import com.musify.app.ui.components.MiniPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    val appBarState = rememberTopAppBarState()
    val playerBottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)


    var playerExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(

        bottomBar = {
            Column {
                MiniPlayer(
                    song = defaultSong
                ) {
                    playerExpanded = true
                }

                HorizontalDivider()
                BottomNavigationBar(navController = navController)

            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = NavScreen.Home.route
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
        }


        if(playerExpanded){
            PlayerScreen(playerBottomSheet){
                playerExpanded = false

            }
        }

    }

}

