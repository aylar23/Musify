package com.musify.app.presentation.search

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.musify.app.R
import com.musify.app.Search
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Playlist.Companion.PLAYLIST
import com.musify.app.domain.models.Song
import com.musify.app.presentation.myplaylist.LibraryContentChipsView
import com.musify.app.presentation.myplaylist.LibrarySection
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.ui.components.SearchBar
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.presentation.search.components.SearchKeysView
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.PlaylistListView
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.utils.BaseUIState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    searchViewModel: SearchViewModel,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Long) -> Unit,
    navigateToPlaylist: (Long) -> Unit,
    navigateToNewPlaylist: () -> Unit
) {
    lateinit var selectedSong: Song

    var settingsClicked by remember {
        mutableStateOf(false)
    }
    val playlists by searchViewModel.getAllPlaylists().collectAsState(initial = emptyList())

    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uiState by searchViewModel.uiState.collectAsState(BaseUIState())


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val searchStr by searchViewModel.searchStr.collectAsState()
    val keys = searchViewModel.keys.collectAsState(initial = Search.getDefaultInstance())
    val focusRequester = remember { FocusRequester() }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage = stringResource(id = R.string.successfully_added)
    val type by searchViewModel.type.collectAsState("")

    val sections = listOf(
        LibrarySection.AllSearch,
        LibrarySection.Artist,
        LibrarySection.Playlist,
        LibrarySection.Album,
        LibrarySection.Song,
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = Modifier
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            },
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


        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(1.dp))

            SearchBar(focusRequester = focusRequester,

                searchStr = searchStr,
                enabled = true,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    focusRequester.requestFocus()
                },
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    searchViewModel.saveSearchStr(searchStr.trim())
                },
                onClear = {
                    searchViewModel.setSearch("")
                    searchViewModel.search("")
                }
            ) {
                searchViewModel.setSearch(it)
                searchViewModel.search(it)
            }

            if (searchStr.isEmpty()) {
                SearchKeysView(keys = keys.value.messageList,
                    onDelete = { searchViewModel.clearSearchStr() },
                    onSearch = {
                        searchViewModel.setSearch(it)
                        searchViewModel.search(it)
                    })
            } else {

                LibraryContentChipsView(
                    sections = sections,
                    selected = type) { type ->
                    searchViewModel.setType(type)
                    searchViewModel.search(searchStr)
                }

                when {
                    uiState.isLoading -> {
                        LoadingView(
                            Modifier
                                .weight(1f)
                                .fillMaxSize(1f)
                        )
                    }

                    uiState.isFailure -> {
                        NetworkErrorView(Modifier.weight(1f)) {
                            searchViewModel.search(searchStr)
                        }
                    }

                    uiState.isSuccess -> {

                        uiState.data?.let { mainScreenData ->

                            ArtistListView(
                                header = R.string.artists, mainScreenData.artists
                            ) { artist ->
                                navigateToArtist(artist)
                            }

                            SongListView(mainScreenData.songs,
                                playerController = searchViewModel.getPlayerController(),
                                downloadTracker = searchViewModel.getDownloadTracker(),
                                onMoreClicked = {
                                    selectedSong = it
                                    settingsClicked = true
                                },
                                onSwipe = { song ->
                                    searchViewModel.getPlayerController().onPlayNext(song)

                                }) { song ->
                                searchViewModel.getPlayerController()
                                    .init(song, mainScreenData.songs)

                            }


                            AlbumListView(playlists = mainScreenData.albums) { album ->
                                navigateToAlbum(album.playlistId)
                            }

                            PlaylistListView(
                                title = stringResource(id = R.string.playlists),
                                playlists =  mainScreenData.playlists
                            ) { playlist ->
                                navigateToPlaylist(playlist.playlistId)
                            }

                        }
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
                },
                onNavigateToArtist = {
                    if (selectedSong.artists.size == 1) {
                        navigateToArtist(selectedSong.getArtist())
                    } else {
                        showArtistDialog = true
                    }
                },
                onPlayNext = {
                    searchViewModel.getPlayerController().selectedTrack?.let {
                        searchViewModel.getPlayerController().onPlayNext(
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
                    searchViewModel.addSongToPlaylist(selectedSong, playlist)
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
                    searchViewModel.addNewPlaylist(name)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
            }

        }

        if (showArtistDialog) {
            ArtistBottomSheet(
                selectedSong = selectedSong,
                artists = selectedSong.artists ?: emptyList(),
                sheetState = artistsSheetState,
                onSelect = { artist -> navigateToArtist(artist) },
                onDismiss = { showArtistDialog = false }

            )

        }
    }
}

