package com.musify.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.navigation.screen.Screen
import com.musify.app.presentation.artist.ArtistScreen
import com.musify.app.presentation.home.HomeScreen
import com.musify.app.presentation.playlist.PlaylistScreen

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
                },
                navigateToAlbum = {
                    innerNavController.navigate(Screen.Playlist.route)

                },
                navigateToPlaylist = {
                    innerNavController.navigate(Screen.Playlist.route)
                },
                navigateToSettings = {},
                navigateToNewPlaylist = {},
            )


        }

        composable(Screen.Artist.route) {

            ArtistScreen(
                paddingValues = paddingValues,
                artistViewModel = hiltViewModel(),
                navigateToArtist = {
                    innerNavController.navigate(Screen.Artist.route)
                },
                navigateToAlbum = {
                    innerNavController.navigate(Screen.Playlist.route)
                },
                navigateToNewPlaylist = {},
                navigateUp = {innerNavController.navigateUp()}

            )


        }

        composable(Screen.Playlist.route) {

            PlaylistScreen(
                paddingValues = paddingValues,
                navigateToNewPlaylist = {},
                navigateToArtist = {
                    innerNavController.navigate(Screen.Artist.route)
                },
                navigateToAlbum = {
                    innerNavController.navigate(Screen.Playlist.route)
                },
                navigateUp = {innerNavController.navigateUp()}
            )


        }
    }

}