package com.musify.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.musify.app.MainActivity.Companion.ALBUMS
import com.musify.app.MainActivity.Companion.PLAYLISTS
import com.musify.app.MainActivity.Companion.TOPS
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
                navigateToArtist = {artist->
                    innerNavController.navigate(Screen.Artist.route+"/${artist.id}")
                },
                navigateToAlbum = {album ->
                    innerNavController.navigate(Screen.Playlist.route+"/$ALBUMS/${album.id}")

                },
                navigateToPlaylist = {playlist ->
                    innerNavController.navigate(Screen.Playlist.route+"/$TOPS/${playlist.id}")
                },
                navigateToSettings = {},
                navigateToNewPlaylist = {},
            )


        }

        composable(route = Screen.Artist.route + "/{id}", arguments = listOf(
            navArgument("id") { type = NavType.LongType },
        )
        ) { entry ->
            entry.arguments?.getLong("id")?.let { id ->
                ArtistScreen(
                    id = id,
                    paddingValues = paddingValues,
                    artistViewModel = hiltViewModel(),
                    navigateToArtist = { artist->
                            innerNavController.navigate(Screen.Artist.route+"/${artist.id}")

                    },
                    navigateToAlbum = { album ->
                        innerNavController.navigate(Screen.Playlist.route+"/$ALBUMS/${album.id}")
                    },
                    navigateToNewPlaylist = {},
                    navigateUp = { innerNavController.navigateUp() }

                )
            }

        }

        composable(route = Screen.Playlist.route + "/{type}/{id}",
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("id") { type = NavType.LongType },
                )
        ){ entry ->

            val id =  entry.arguments?.getLong("id") ?: 0L
            val type =  entry.arguments?.getString("type") ?: PLAYLISTS
            PlaylistScreen(
                id = id,
                type = type,
                paddingValues = paddingValues,
                playlistViewModel = hiltViewModel(),
                navigateToNewPlaylist = {},
                navigateToArtist = { artist->
                    innerNavController.navigate(Screen.Artist.route+"/${artist.id}")

                },
                navigateToAlbum = { album ->
                    innerNavController.navigate(Screen.Playlist.route+"/$ALBUMS/${album.id}")
                },
                navigateUp = {innerNavController.navigateUp()}
            )


        }
    }


}