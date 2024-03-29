package com.musify.app.presentation.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import com.google.common.base.Preconditions
import com.musify.app.MainActivity
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.artist.ArtistViewModel
import com.musify.app.presentation.destinations.ArtistScreenDestination
import com.musify.app.presentation.destinations.PlaylistScreenDestination
import com.musify.app.presentation.destinations.SearchScreenDestination
import com.musify.app.presentation.destinations.SettingsScreenDestination
import com.musify.app.presentation.home.components.HomeTopAppBar
import com.musify.app.presentation.myplaylist.MyPlaylistsViewModel
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.SearchBar
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.PlaylistListView
import com.musify.app.ui.components.listview.SongGridListView
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.WhiteTextColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    val homeViewModel = hiltViewModel<HomeViewModel>()

    val uiState by homeViewModel.uiState.collectAsState()

    lateinit var selectedSong: Song
    var settingsClicked by remember {
        mutableStateOf(false)
    }


    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }
    val playlists by homeViewModel.getAllPlaylists().collectAsState(initial = emptyList())

    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage = stringResource(id = R.string.successfully_added)

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Inactive,
                    contentColor = WhiteTextColor,
                    snackbarData = data
                )
            }
        },
    ) { padding ->


        LazyColumn(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {

                HomeTopAppBar(
                    onSearchClicked = { navigator.navigate(SearchScreenDestination) },
                    onSettingsClicked = { navigator.navigate(SettingsScreenDestination) }
                )
            }

            if (uiState.isSuccess) {
                uiState.data?.let { mainScreenData ->

                    item {
                        PlaylistListView(
                            title = stringResource(id = R.string.top),
                            playlists = mainScreenData.tops
                        ) { playlist ->
                            navigator.navigate(
                                PlaylistScreenDestination(playlist.playlistId, MainActivity.TOPS)
                            )
                        }
                    }

                    item {


                        ArtistListView(
                            header = R.string.artists, mainScreenData.artists
                        ) { artist ->
                            navigator.navigate(
                                ArtistScreenDestination(artist.id)
                            )
                        }


                    }

                    item {


                        AlbumListView(
                            title = stringResource(id = R.string.new_albums),
                            mainScreenData.albums
                        ) { album ->
                            navigator.navigate(
                                PlaylistScreenDestination(album.playlistId, MainActivity.ALBUMS)
                            )
                        }
                    }

                    item {
                        SongGridListView(
                            homeViewModel = homeViewModel,
                            songs = mainScreenData.songs,
                            onMoreClicked = {
                                selectedSong = it
                                settingsClicked = true
                            }
                        ) { song ->
                            homeViewModel.getPlayerController().init(song, mainScreenData.songs)
                        }

                    }

                    items(mainScreenData.playlistCategory) { playlistCategory ->
                        PlaylistListView(
                            title = playlistCategory.name,
                            playlists = playlistCategory.playlists
                        ) { playlist ->
                            navigator.navigate(
                                PlaylistScreenDestination(playlist.playlistId, MainActivity.PLAYLISTS)
                            )
                        }

                    }
                }
            }

        }
        when {


            uiState.isLoading -> {

                LoadingView(Modifier.fillMaxSize(1f))

            }

            uiState.isFailure -> {

                NetworkErrorView(Modifier.fillMaxSize(1f)) {
                    homeViewModel.getMainPageData()
                }


            }
        }
        if (settingsClicked) {
            TrackBottomSheet(
                selectedSong = selectedSong,
                songSettingsSheetState = songSettingsSheetState,
                onAddToPlaylist = {
                    settingsClicked = false
                    addToPlaylistClicked = true
                },
                onNavigateToAlbum = {
                    selectedSong.albumId?.let {
                        navigator.navigate(
                            PlaylistScreenDestination(it, MainActivity.ALBUMS)
                        )
                    }
                },
                onNavigateToArtist = {
                    if (selectedSong.artists.size == 1) {
                        selectedSong.getArtist().id.let {
                            navigator.navigate(
                                ArtistScreenDestination(it)
                            )
                        }
                    } else {
                        showArtistDialog = true
                    }
                },
                onPlayNext = {
                    homeViewModel.getPlayerController().selectedTrack?.let {
                        homeViewModel.getPlayerController().onPlayNext(
                            it
                        )
                    }
                },
                onShare = {},
            ) {
                settingsClicked = false
            }
        }





        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(
                selectedSong = selectedSong,
                playlists = playlists,
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    showNewPlaylistDialog = true
                },
                onSelect = { playlist ->
                    homeViewModel.addSongToPlaylist(selectedSong, playlist)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
            ) {

                addToPlaylistClicked = false
            }
        }

        if (showNewPlaylistDialog) {
            Dialog(onDismissRequest = { showNewPlaylistDialog = false }) {
                NewPlaylistDialog() { name ->
                    showNewPlaylistDialog = false
                    homeViewModel.addNewPlaylist(name)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
            }

        }

        if (showArtistDialog) {
            ArtistBottomSheet(
                selectedSong = selectedSong,
                artists = selectedSong.artists,
                sheetState = artistsSheetState,
                onSelect = { artist -> navigator.navigate(ArtistScreenDestination(artist.id)) },
                onDismiss = { showArtistDialog = false }

            )

        }


    }
}

