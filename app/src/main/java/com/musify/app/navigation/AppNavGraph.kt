package com.musify.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.presentation.home.HomeScreen
import com.musify.app.presentation.myplaylist.MyPlaylistsScreen
import com.musify.app.presentation.search.SearchScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = NavScreen.Home.route
        ) {
            composable(NavScreen.Home.route) {
                HomeScreen(
                    paddingValues =  paddingValues,
                    homeViewModel = hiltViewModel(),
                )

            }
            composable(NavScreen.Search.route) {
                SearchScreen(
                    paddingValues =  paddingValues,
                    searchViewModel = hiltViewModel()
                )
            }
            composable(NavScreen.MyPlaylists.route) {

                MyPlaylistsScreen(
                    paddingValues =  paddingValues,
                    myPlaylistsViewModel = hiltViewModel(),
                )

            }
        }
    }

}

