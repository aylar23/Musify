package com.musify.app.presentation.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Album
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultArtist
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.domain.models.mainScreenData
import com.musify.app.presentation.playlist.components.CollapsingTopAppBar
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    paddingValues: PaddingValues,
    artistViewModel: ArtistViewModel,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Album) -> Unit,
    navigateToNewPlaylist: () -> Unit,
    navigateUp: () -> Unit,
) {

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    lateinit var selectedSong :  Song
    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier.padding(paddingValues = paddingValues),
        topBar = {
            CollapsingTopAppBar(
                title = defaultArtist.name,
                scrollBehaviour = scrollBehavior
            ){
                navigateUp()
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .graphicsLayer {
                translationY = scrollBehavior.state.contentOffset
            }) {
            Image(
                painter = painterResource(id = R.drawable.mock_cover),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )
        }



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            item {
                Column(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            startY = 0f, endY = 240f, colors = listOf(
                                TransparentColor, AlbumCoverBlackBG
                            )
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp)
                            .padding(vertical = 14.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TNT Music App",
                            color = WhiteTextColor,
                            fontFamily = SFFontFamily,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }
            }


            item {
                SongListView(mainScreenData.hitSongs, onMoreClicked = {
                    selectedSong = it
                    settingsClicked = true
                }) {

                }
            }


            item {
                AlbumListView(mainScreenData.albums) { album ->
                    navigateToAlbum(album)
                }
            }


            item {
                SongListView(mainScreenData.hitSongs, onMoreClicked = {
                    selectedSong = it
                    settingsClicked = true
                }) {

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
                    navigateToArtist(selectedSong.artist)
                },
                onPlayNext = {},
                onShare = {},
            ) {
                settingsClicked = false
            }
        }



        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(playlists = mutableListOf(defaultPlaylist),
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    navigateToNewPlaylist()
                }) {
                addToPlaylistClicked = false
            }
        }

    }


}

