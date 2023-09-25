package com.musify.app.presentation.localplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Album
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.domain.models.defaultSong
import com.musify.app.domain.models.mainScreenData
import com.musify.app.ui.components.CollapsingSmallTopAppBar
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.SongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalPlaylistScreen(
    paddingValues: PaddingValues,
    navigateToNewPlaylist: () -> Unit,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Album) -> Unit,
    navigateUp: () -> Unit,
) {
    val playlist = defaultPlaylist

    lateinit var selectedSong: Song
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

    var settingsClicked by remember{
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(
            AlbumCoverBlackBG
        ),
        topBar = { CollapsingSmallTopAppBar(
            scrollBehaviour = scrollBehavior,
            trailingIcon = R.drawable.search,
            trailingIconDescription = stringResource(
            id = R.string.search )) {
            navigateUp() } })
    { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                Column(
                    modifier = Modifier.background(
                     AlbumCoverBlackBG
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = playlist.name,
                            color = WhiteTextColor,
                            fontFamily = SFFontFamily,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = R.string.play_all,
                            onClick = { },
                            containerColor = Yellow,
                            contentColor = Black,
                        )

                        Spacer(modifier = Modifier.weight(.1f))

                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = R.string.shuffle,
                            onClick = { },
                            containerColor = DarkGray,
                            contentColor = WhiteTextColor,
                            leadingIcon = R.drawable.play
                        )


                    }
                }

            }

            items(mainScreenData.hitSongs) { song ->
                SongView(
                    song = defaultSong,
                    onMoreClicked = {
                        selectedSong = song
                        settingsClicked = true
                    }
                ) {

                }
            }

        }

        if (settingsClicked){
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
            ){
                settingsClicked = false
            }
        }



        if (addToPlaylistClicked){
            AddToPlaylistBottomSheet(
                playlists = mutableListOf(defaultPlaylist),
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
