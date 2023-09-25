package com.musify.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.musify.app.navigation.screen.Screen
import com.musify.app.presentation.artist.ArtistScreen
import com.musify.app.presentation.myplaylist.MyPlaylistsScreen
import com.musify.app.presentation.playlist.PlaylistScreen
import com.musify.app.presentation.localplaylist.LocalPlaylistScreen


@Composable
fun LibraryNavGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {

    val innerNavController = rememberNavController()


    NavHost(
        navController = innerNavController,
        startDestination = Screen.MyPlaylist.route
    ) {
        composable(Screen.MyPlaylist.route) {

            MyPlaylistsScreen(
                paddingValues = paddingValues,
                myPlaylistsViewModel = hiltViewModel(),
                navigateToNewPlaylist = {},
                navigateToLocalPlaylist = {
                    innerNavController.navigate(Screen.LocalPlaylist.route)

                }
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
                navigateUp = {
                    innerNavController.navigateUp()
                }
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

        composable(Screen.LocalPlaylist.route) {

            LocalPlaylistScreen(
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