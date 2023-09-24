package com.musify.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.musify.app.presentation.artist.ArtistScreen
import com.musify.app.presentation.home.HomeScreen

@Composable
fun HomeNavGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {

    val innerNavController = rememberNavController()
    NavHost(
        navController = innerNavController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {

            HomeScreen(
                paddingValues = paddingValues,
                homeViewModel = hiltViewModel(),
                navigateToSearch = {
                    navController.navigate(NavScreen.Search.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateToArtist = {
                    innerNavController.navigate(Screen.Artist.route)
                }
            )


        }

        composable(Screen.Artist.route) {

            ArtistScreen(
                paddingValues = paddingValues,
                artistViewModel = hiltViewModel(),

            )


        }
    }

}