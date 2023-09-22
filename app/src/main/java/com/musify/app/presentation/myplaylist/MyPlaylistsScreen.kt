package com.musify.app.presentation.myplaylist

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.presentation.common.PlaylistView
import com.musify.app.presentation.songsinplaylist.SelectedPlaylistScreen
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.WhiteTextColor

//Temporary
var playlists = mutableListOf<Playlist>(defaultPlaylist)


@Composable
fun MyPlaylistsScreen(
    paddingValues: PaddingValues,
    myPlaylistsViewModel: MyPlaylistsViewModel
) {

    var playlistSelected by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedPlaylist: Playlist?


    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(AlbumCoverBlackBG)){
        LazyColumn(modifier = Modifier.fillMaxSize()){
            item{
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(modifier = Modifier.weight(1f), text = stringResource(id = R.string.my_playlists), fontSize = 22.sp, color = WhiteTextColor, fontWeight = FontWeight.Bold)
                    Icon(modifier = Modifier, painter = painterResource(id = R.drawable.add), tint = WhiteTextColor, contentDescription = stringResource(
                        id = R.string.create_new_playlist
                    )
                    )
                }
            }

            items(playlists.size){ index ->
                Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                    PlaylistView(playlist = playlists[index]) {
                        playlistSelected = true
                        selectedPlaylist = playlists[index]
                        Log.i("selectedPlaylist", "${selectedPlaylist}")
                    }
                }
            }
        }


        AnimatedVisibility(
            visible = playlistSelected,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(200)),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(200))
        ) {
            SelectedPlaylistScreen(defaultPlaylist) { playlistSelected = false }
        }



    }
}
