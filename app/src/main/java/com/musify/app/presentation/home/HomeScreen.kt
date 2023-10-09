package com.musify.app.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.home.components.HomeTopAppBar
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.SearchBar
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.ArtistListView
import com.musify.app.ui.components.listview.PlaylistListView
import com.musify.app.ui.components.listview.SongGridListView


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    navigateToSearch: () -> Unit,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Playlist) -> Unit,
    navigateToPlaylist: (Playlist) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToNewPlaylist: () -> Unit,
) {


    val uiState by homeViewModel.uiState.collectAsState()

    lateinit var selectedSong: Song
    var settingsClicked by remember {
        mutableStateOf(false)
    }



    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier.padding(paddingValues),
    ) { padding ->


        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            HomeTopAppBar { navigateToSettings() }

            SearchBar(
                enabled = false,
                onClick = navigateToSearch,
            ) {}



            when {
                uiState.isLoading -> {
                    LoadingView(Modifier.weight(1f).fillMaxSize(1f))
                }

                uiState.isFailure -> {
                    NetworkErrorView(Modifier.weight(1f)) {
                        homeViewModel.getMainPageData()
                    }
                }

                uiState.isSuccess ->{

                    uiState.data?.let { mainScreenData ->
                        PlaylistListView(mainScreenData.tops) { playlist ->
                            navigateToPlaylist(playlist)
                        }

                        ArtistListView(
                            header = R.string.artists,
                            mainScreenData.artists
                        ) { artist ->  navigateToArtist(artist) }


                        AlbumListView(mainScreenData.albums) { album ->
                            navigateToAlbum(album)
                        }


                        SongGridListView(
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