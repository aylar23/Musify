package com.musify.app.ui.components.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistBottomSheet(
    playlists: MutableList<Playlist>,
    playlistSheetState : SheetState,
    onCreateNewPlaylist: ()->Unit,
    onDismiss: ()->Unit
) {
    ModalBottomSheet(containerColor = AlbumCoverBlackBG,
        sheetState = playlistSheetState,
        onDismissRequest = onDismiss) {
        for(playlist in playlists){
            ActionsModelView(icon = R.drawable.playlist_add_check, mainText = playlist.name, grayText = playlist.year.toString(), expandable = false){}
        }
        Box(modifier = Modifier.padding(10.dp)){
            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.create_new_playlist,
                onClick = { onCreateNewPlaylist() },
                containerColor = Yellow,
                contentColor = AlbumCoverBlackBG
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}