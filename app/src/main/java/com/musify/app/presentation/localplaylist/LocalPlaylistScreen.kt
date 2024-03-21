package com.musify.app.presentation.localplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import com.musify.app.MainActivity
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.destinations.ArtistScreenDestination
import com.musify.app.presentation.destinations.PlaylistScreenDestination
import com.musify.app.presentation.myplaylist.MyPlaylistsViewModel
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.ui.components.CollapsingSmallTopAppBar
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.SwipeableSongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun LocalPlaylistScreen(
    id: Long,
    navigator: DestinationsNavigator
) {

    val localPlaylistViewModel = hiltViewModel<LocalPlaylistViewModel>()

    val playlist by localPlaylistViewModel.getPlaylist(id).collectAsState(initial = null)


    lateinit var selectedSong: Song
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlists by localPlaylistViewModel.getAllPlaylists().collectAsState(initial = emptyList())

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
                title = playlist?.playlist?.name ?: "",
                trailingIcon = if (playlist?.playlist?.downloadable != true) R.drawable.ic_download else R.drawable.ic_downloaded,
                onIconClick = {

                    playlist?.let { playlist ->

                        localPlaylistViewModel.updateDownloadStatus(
                            playlist, !playlist.playlist.downloadable
                        )

                    }

                }) {
                navigator.navigateUp()
            }
        }) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    CustomButton(
                        modifier = Modifier.weight(1f),
                        text = R.string.play_all,
                        onClick = {

                            playlist?.songs?.let { data ->

                                if (data.isNotEmpty()) {
                                    localPlaylistViewModel.getPlayerController()
                                        .init(data[0], data)
                                }

                            }

                        },
                        containerColor = Inactive,
                        contentColor = Yellow,
                        leadingIcon = R.drawable.play,
                        shape = MaterialTheme.shapes.medium

                    )

                    Spacer(modifier = Modifier.weight(.1f))

                    CustomButton(
                        modifier = Modifier.weight(1f),
                        text = R.string.shuffle,
                        onClick = {
                            playlist?.songs?.let { data ->
                                if (data.isNotEmpty()) {
                                    localPlaylistViewModel.getPlayerController()
                                        .init(data[0], data)
                                }
                            }
                        },
                        containerColor = Inactive,
                        contentColor = Yellow,
                        leadingIcon = R.drawable.shuffle,
                        shape = MaterialTheme.shapes.medium

                    )


                }


            }
            if (playlist == null) {

                item {

                    LoadingView(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Background)
                    )
                }

            } else {


                playlist?.songs?.let { data ->
                    items(data) { song ->


                        SwipeableSongView(
                            song = song,
                            playerController = localPlaylistViewModel.getPlayerController(),
                            downloadTracker = localPlaylistViewModel.getDownloadTracker(),
                            onMoreClicked = {
                                selectedSong = song
                                settingsClicked = true
                            }, onSwipe = {
                                localPlaylistViewModel.getPlayerController().onPlayNext(song)

                            }) {
                            localPlaylistViewModel.getPlayerController().init(song, data)

                        }
                    }
                }

            }


        }

        if (settingsClicked) {
            TrackBottomSheet(
                deletable = true,
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
                    localPlaylistViewModel.getPlayerController().selectedTrack?.let {
                        localPlaylistViewModel.getPlayerController().onPlayNext(
                            it
                        )
                    }
                },
                onShare = {},
                onDelete = {
                    playlist?.let { playlist ->
                        localPlaylistViewModel.deleteSongFromPlaylist(
                            playlist.playlist,
                            selectedSong
                        )
                    }
                },
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
                    localPlaylistViewModel.addSongToPlaylist(selectedSong, playlist)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
            ) {

                addToPlaylistClicked = false
            }
        }

        if (showNewPlaylistDialog) {
            Dialog(
                onDismissRequest = { showNewPlaylistDialog = false }
            ) {
                NewPlaylistDialog() { name ->
                    showNewPlaylistDialog = false
                    localPlaylistViewModel.addNewPlaylist(name)
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
