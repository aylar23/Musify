package com.musify.app.presentation.song

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.musify.app.MainActivity
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.destinations.ArtistScreenDestination
import com.musify.app.presentation.destinations.PlaylistScreenDestination
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.ui.components.CollapsingSmallTopAppBar
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.NotFoundView
import com.musify.app.ui.components.SwipeableSongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.WhiteTextColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SongsScreen(
    artistId: Long,
    isTop: Int,
    isSingle: Int,
    navigator: DestinationsNavigator
) {

    val songsViewModel = hiltViewModel<SongsViewModel>()

    LaunchedEffect(artistId) {
        songsViewModel.setArtistId(artistId)
        songsViewModel.setIsSingle(isSingle)
        songsViewModel.setIsTop(isTop)
    }
    lateinit var selectedSong: Song

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }
    val playlists by songsViewModel.getAllPlaylists().collectAsState(initial = emptyList())

    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage = stringResource(id = R.string.successfully_added)

    val songs = songsViewModel.songs.collectAsLazyPagingItems()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            AlbumCoverBlackBG
        ),

        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Inactive,
                    contentColor = WhiteTextColor,
                    snackbarData = data
                )
            }
        },
        topBar = {

            CollapsingSmallTopAppBar(
                title = stringResource(id = R.string.songs)
            ) {
                navigator.navigateUp()
            }

        }
    ) { padding ->

        if (songs.itemCount == 0 && songs.loadState.refresh == LoadState.Loading) {
            LoadingView(Modifier.fillMaxSize())
        } else if (songs.itemCount == 0 && songs.loadState.refresh is LoadState.Error) {
            NetworkErrorView(Modifier.fillMaxSize()) {
                songs.retry()
                songs.refresh()
            }
        } else if (songs.itemCount == 0) {
            NotFoundView(Modifier.fillMaxSize())
        } else {

            LazyColumn(modifier = Modifier.fillMaxSize().padding(top = padding.calculateTopPadding())) {
                items(songs) { song ->

                    SwipeableSongView(song = song!!,
                        playerController = songsViewModel.getPlayerController(),
                        onMoreClicked = {
                            selectedSong = it
                            settingsClicked = true
                        },
                        downloadTracker = songsViewModel.getDownloadTracker(),

                        onSwipe = {
                            songsViewModel.getPlayerController().onPlayNext(song)
                        }
                    ) {
                        songsViewModel.getPlayerController().init(song, songs.itemSnapshotList.items)
                    }
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
                        selectedSong.getArtist().id.let { navigator.navigate(
                            ArtistScreenDestination(it)
                        )}
                    } else {
                        showArtistDialog = true
                    }
                },
                onPlayNext = {
                    songsViewModel.getPlayerController().selectedTrack?.let {
                        songsViewModel.getPlayerController().onPlayNext(
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
                    songsViewModel.addSongToPlaylist(selectedSong, playlist)
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
                    songsViewModel.addNewPlaylist(name)
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