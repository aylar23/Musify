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
import com.musify.app.MainActivity
import com.musify.app.navigation.screen.Screen
import com.musify.app.presentation.artist.ArtistScreen
import com.musify.app.presentation.search.SearchScreen
import com.musify.app.presentation.playlist.PlaylistScreen


@Composable
fun SearchNavGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    val innerNavController = rememberNavController()
    NavHost(
        navController = innerNavController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {

            SearchScreen(
                paddingValues = paddingValues,
                searchViewModel = hiltViewModel(),
                navigateToArtist = { artist->
                    innerNavController.navigate(Screen.Artist.route+"/${artist.id}")
                },
                navigateToAlbum = { album ->
                    innerNavController.navigate(Screen.Playlist.route+"/${MainActivity.ALBUMS}/${album.id}")
                },
                navigateToPlaylist = {playlist ->
                    innerNavController.navigate(Screen.Playlist.route+"/${MainActivity.PLAYLISTS}/${playlist.id}")
                },
                navigateToNewPlaylist = {}
            )


        }

        composable(
            route = Screen.Artist.route + "/{id}", arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) { entry ->
            entry.arguments?.getLong("id")?.let { id ->
                ArtistScreen(
                    id = id,
                    paddingValues = paddingValues,
                    artistViewModel = hiltViewModel(),
                    navigateToArtist = { artist ->
                        innerNavController.navigate(Screen.Artist.route + "/${artist.id}")

                    },
                    navigateToAlbum = { album ->
                        innerNavController.navigate(Screen.Playlist.route+"/${MainActivity.ALBUMS}/${album.id}")
                    },
                    navigateToNewPlaylist = {},
                    navigateUp = {
                        innerNavController.navigateUp()
                    },

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
            val type =  entry.arguments?.getString("type") ?: MainActivity.PLAYLISTS
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
                    innerNavController.navigate(Screen.Playlist.route+"/${MainActivity.ALBUMS}/${album.id}")
                },
                navigateUp = {
                    innerNavController.navigateUp()
                }
            )

        }
    }

}