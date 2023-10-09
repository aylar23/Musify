package com.musify.app.presentation.search

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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.SearchBar
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.presentation.search.components.SearchKeysView
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.utils.BaseUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    searchViewModel: SearchViewModel,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Playlist) -> Unit,
    navigateToPlaylist: (Playlist) -> Unit,
    navigateToNewPlaylist: () -> Unit,
) {
    lateinit var selectedSong: Song
    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()
    val uiState by searchViewModel.uiState.collectAsState(BaseUIState())


    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val searchStr = searchViewModel.searchStr.collectAsState()
    val keys = remember {
        mutableStateListOf("wer", "wer1", "wer2", "wer3", "wer4")
    }
    Scaffold(
        modifier = Modifier.padding(paddingValues)
    ) { padding ->


        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(1.dp))

            SearchBar(
                searchStr = searchStr,
                enabled = true,
                onClick = {},
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ) {
                searchViewModel.setSearch(it)
                searchViewModel.search(it)
            }

            if (searchStr.value.isEmpty()) {
                SearchKeysView(
                    keys = keys,
                    onDelete = { keys.remove(it) },
                    onSearch = {
                       searchViewModel.setSearch(it)
                        searchViewModel.search(it)
                    }
                )
            } else {


                when {
                    uiState.isLoading -> {
                        LoadingView(
                            Modifier
                                .weight(1f)
                                .fillMaxSize(1f))
                    }

                    uiState.isFailure -> {
                        NetworkErrorView(Modifier.weight(1f)) {
                            searchViewModel.search(searchStr.value)
                        }
                    }

                    uiState.isSuccess ->{

                        uiState.data?.let { mainScreenData ->
                            ArtistListView(
                                header = R.string.artists,
                                mainScreenData.artists
                            ) { artist -> navigateToArtist(artist) }



                            SongListView(
                                mainScreenData.songs,
                                onMoreClicked = {
                                    selectedSong = it
                                    settingsClicked = true
                                }
                            ) {

                            }

                        }
                    }
                }


            }


        }

        if (settingsClicked) {
            TrackBottomSheet(
                songSettingsSheetState = songSettingsSheetState,
                onAddToPlaylist = {
                    settingsClicked = false
                    addToPlaylistClicked = true
                },
                onNavigateToAlbum = {
                    navigateToAlbum(selectedSong.album)
                },
                onNavigateToArtist = {
                    navigateToArtist(selectedSong.getArtist())
                },
                onPlayNext = {},
                onShare = {},
            ) {
                settingsClicked = false
            }
        }



        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(
                playlists = mutableListOf(),
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    navigateToNewPlaylist()
                }
            ) {
                addToPlaylistClicked = false
            }
        }

    }
}

