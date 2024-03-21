package com.musify.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.musify.app.presentation.albums.AlbumsScreen
import com.musify.app.presentation.artist.ArtistScreen
import com.musify.app.presentation.home.HomeScreen
import com.musify.app.presentation.playlist.PlaylistScreen
import com.musify.app.presentation.song.SongsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNavGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {

//    val innerNavController = rememberNavController()
//    NavHost(
//        navController = innerNavController,
//        startDestination = Screen.Home.route,
//        enterTransition = { fadeIn(animationSpec = tween(100)) },
//        exitTransition = { fadeOut(animationSpec = tween(100)) },
//        popEnterTransition = { fadeIn(animationSpec = tween(100)) },
//        popExitTransition = { fadeOut(animationSpec = tween(100)) },
//    ) {
//        composable(Screen.Home.route) {
//
//
//            HomeScreen(
//                paddingValues = paddingValues,
//                homeViewModel =hiltViewModel(),
//                navigateToSearch = {
//                    navController.navigate(NavScreen.Search.route) {
//
//                        navController.graph.startDestinationRoute?.let { screen_route ->
//                            popUpTo(screen_route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                },
//                navigateToArtist = { artist ->
//                    innerNavController.navigate(Screen.Artist.route + "/${artist.id}")
//                },
//                navigateToAlbum = { albumId ->
//                    innerNavController.navigate(Screen.Playlist.route + "/$ALBUMS/${albumId}")
//
//                },
//                navigateToPlaylist = { playlist ->
//                    innerNavController.navigate(Screen.Playlist.route + "/$PLAYLISTS/${playlist.playlistId}")
//                },
//                navigateToTopPlaylist = { playlist ->
//                    innerNavController.navigate(Screen.Playlist.route + "/$TOPS/${playlist.playlistId}")
//                },
//                navigateToSettings = {
//                    navController.navigate(Screen.Settings.route)
//                },
//                navigateToNewPlaylist = {},
//            )
//
//
//        }
//
//        composable(
//            route = Screen.Artist.route + "/{id}", arguments = listOf(
//                navArgument("id") { type = NavType.LongType },
//            )
//        ) { entry ->
//            entry.arguments?.getLong("id")?.let { id ->
//                ArtistScreen(
//                    id = id,
//                    paddingValues = paddingValues,
//                    artistViewModel = hiltViewModel(),
//                    navigateToArtist = { artist ->
//                        innerNavController.navigate(Screen.Artist.route + "/${artist.id}")
//                    },
//                    navigateToAlbum = { album ->
//                        innerNavController.navigate(Screen.Playlist.route + "/$ALBUMS/${album}")
//                    },
//                    navigateToAlbums = { artist ->
//                        innerNavController.navigate(Screen.Albums.route + "/${artist.id}")
//                    },
//                    navigateToTops = { artist ->
//                        innerNavController.navigate(Screen.Songs.route + "/${artist.id}/1/0")
//                    },
//                    navigateToSingles = { artist ->
//                        innerNavController.navigate(Screen.Songs.route + "/${artist.id}/0/1")
//                    },
//                    navigateUp = { innerNavController.navigateUp() }
//
//                )
//            }
//
//        }
//
//        composable(
//            route = Screen.Playlist.route + "/{type}/{id}",
//            arguments = listOf(
//                navArgument("type") { type = NavType.StringType },
//                navArgument("id") { type = NavType.LongType },
//            )
//        ) { entry ->
//
//            val id = entry.arguments?.getLong("id") ?: 0L
//            val type = entry.arguments?.getString("type") ?: PLAYLISTS
//            PlaylistScreen(
//                id = id,
//                type = type,
//                paddingValues = paddingValues,
//                playlistViewModel = hiltViewModel(),
//                navigateToNewPlaylist = {},
//                navigateToArtist = { artist ->
//                    innerNavController.navigate(Screen.Artist.route + "/${artist.id}")
//
//                },
//                navigateToAlbum = { album ->
//                    innerNavController.navigate(Screen.Playlist.route + "/$ALBUMS/${album}")
//                },
//                navigateUp = { innerNavController.navigateUp() }
//            )
//
//
//        }
//
//        composable(
//            route = Screen.Albums.route + "/{id}",
//            arguments = listOf(
//                navArgument("id") { type = NavType.LongType },
//            )
//        ) { entry ->
//            val id = entry.arguments?.getLong("id") ?: 0L
//
//            AlbumsScreen(
//                artistId = id,
//                paddingValues = paddingValues,
//                albumsViewModel = hiltViewModel(),
//                navigateUp = { innerNavController.navigateUp() },
//            )
//        }
//
//        composable(
//            route = Screen.Songs.route + "/{id}/{isTop}/{isSingle}",
//            arguments = listOf(
//                navArgument("id") { type = NavType.LongType },
//                navArgument("isTop") { type = NavType.IntType },
//                navArgument("isSingle") { type = NavType.IntType },
//            )
//        ) { entry ->
//
//            val id = entry.arguments?.getLong("id") ?: 0L
//            val isTop = entry.arguments?.getInt("isTop") ?: 0
//            val isSingle = entry.arguments?.getInt("isSingle") ?: 0
//
//            SongsScreen(
//                artistId = id,
//                isTop = isTop,
//                isSingle = isSingle ,
//                paddingValues = paddingValues,
//                songsViewModel = hiltViewModel(),
//                navigateToArtist = { artist ->
//                    innerNavController.navigate(Screen.Artist.route + "/${artist.id}")
//                },
//                navigateToAlbum = { album ->
//                    innerNavController.navigate(Screen.Playlist.route + "/$ALBUMS/${album}")
//                }
//            ) { innerNavController.navigateUp() }
//
//
//        }
//    }


}