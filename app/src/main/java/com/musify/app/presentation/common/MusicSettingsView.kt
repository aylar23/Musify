@file:OptIn(ExperimentalMaterial3Api::class)

package com.musify.app.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import kotlinx.coroutines.launch


@Composable
fun ActionsModelView(icon: Int, text: String, expandable: Boolean, playlist: Playlist?, onClick: () -> Unit){
    Row(modifier = Modifier
        .padding(14.dp)
        .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(24.dp), painter = painterResource(id = icon), contentDescription = text + "button", tint = WhiteTextColor)
        if (playlist == null){
            Text(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
        } else {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp), text = playlist.image, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp), text = playlist.songsCount.toString() + " " + R.string.songs, fontSize = 10.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Normal, color = WhiteTextColor)
            }
        }
        if (expandable){
            Icon(modifier = Modifier
                .size(14.dp), painter = painterResource(id = R.drawable.right_arrow), contentDescription = "See more icon", tint = WhiteTextColor)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomSheet(settingsClicked: MutableState<Boolean>, playlists: MutableList<Playlist>) {
    val songSettingssheetState = rememberModalBottomSheetState()
    val playlistSheetState = rememberModalBottomSheetState()
    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = songSettingssheetState,
        onDismissRequest = { settingsClicked.value = false }) {
        ActionsModelView(expandable = true, icon = R.drawable.library_add, text = stringResource(id = R.string.add_to_playlist), playlist = null, onClick = {
            addToPlaylistClicked = true
            coroutineScope.launch {
                songSettingssheetState.hide()
            }
        })
        ActionsModelView(expandable = false, icon = R.drawable.redo, text = stringResource(id = R.string.play_next), playlist = null, onClick = {})
        ActionsModelView(expandable = true, icon = R.drawable.account_circle, text = stringResource(id = R.string.see_the_artist), playlist = null, onClick = {})
        ActionsModelView(expandable = true, icon = R.drawable.album, text = stringResource(id = R.string.see_the_album), playlist = null, onClick = {})
        ActionsModelView(expandable = false, icon = R.drawable.share, text = stringResource(id = R.string.share), playlist = null, onClick = {})

    }

    if (addToPlaylistClicked){
        ModalBottomSheet(containerColor = AlbumCoverBlackBG,
            sheetState = playlistSheetState,
            onDismissRequest = { addToPlaylistClicked = false }) {
            for(playlist in playlists){
                ActionsModelView(icon = R.drawable.playlist_add_check, text = stringResource(id = R.string.songs), expandable = false, playlist = playlist){}
            }
            Box(modifier = Modifier.padding(10.dp)){
                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = R.string.create_new_playlist,
                    onClick = { /*TODO*/ },
                    containerColor = Yellow,
                    contentColor = AlbumCoverBlackBG
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }


}