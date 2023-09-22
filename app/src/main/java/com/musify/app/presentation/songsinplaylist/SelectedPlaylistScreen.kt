package com.musify.app.presentation.songsinplaylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.domain.models.defaultSong
import com.musify.app.presentation.common.CustomButton
import com.musify.app.presentation.common.SongView
import com.musify.app.presentation.common.bottomSheet
import com.musify.app.presentation.songsinplaylist.components.CollapsingSmallTopAppBar
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedPlaylistScreen(playlist: Playlist, goBack: () -> Unit) {

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

    val settingsClicked = remember{
        mutableStateOf(false)
    }


    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(
            AlbumCoverBlackBG
        ),
        topBar = { CollapsingSmallTopAppBar(scrollBehaviour = scrollBehavior) { goBack() } })
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

            items(playlist.songsCount) { id ->
                SongView(song = defaultSong) {
                    settingsClicked.value = true
                }
            }

        }


        if (settingsClicked.value){
            bottomSheet(settingsClicked, mutableListOf(defaultPlaylist))
        }

    }



}
