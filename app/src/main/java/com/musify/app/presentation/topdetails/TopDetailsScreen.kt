package com.musify.app.presentation.topdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
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
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.domain.models.defaultSong
import com.musify.app.presentation.common.AddToPlaylistBottomSheet
import com.musify.app.presentation.common.CustomButton
import com.musify.app.presentation.common.SongView
import com.musify.app.presentation.common.TrackBottomSheet
import com.musify.app.presentation.topdetails.components.CollapsingTopAppBar
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


private var selectedSong: Song? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDetailsScreen(paddingValues: PaddingValues) {

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()

    Scaffold(modifier = Modifier.padding(paddingValues = paddingValues),
        topBar = { CollapsingTopAppBar(scrollBehaviour = scrollBehavior) }) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(AlbumCoverBlackBG)
            .graphicsLayer {
                translationY = scrollBehavior.state.contentOffset
            }) {
            Image(
                painter = painterResource(id = R.drawable.mock_cover),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                            .padding(vertical = 4.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "TNT Music App",
                            color = WhiteTextColor,
                            fontFamily = SFFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedIconButton(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(50.dp),
                            border = BorderStroke(
                                width = 1.dp, color = WhiteTextColor
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.group),
                                tint = WhiteTextColor,
                                contentDescription = ""
                            )
                        }
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

            items(50) { id ->
                SongView(song = defaultSong) {
                    settingsClicked = true
                    selectedSong = defaultSong
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
                onNavigateToAlbum = {},
                onNavigateToArtist = {},
                onPlayNext = {},
                onShare = {},
            ) {
                settingsClicked = false
            }
        }



        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(
                playlists = mutableListOf(defaultPlaylist),
                playlistSheetState = playlistSheetState,
            ) {
                addToPlaylistClicked = false
            }
        }

    }


}

