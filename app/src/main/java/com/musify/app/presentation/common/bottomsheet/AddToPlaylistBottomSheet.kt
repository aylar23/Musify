package com.musify.app.presentation.common.bottomsheet

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.presentation.common.ActionsModelView
import com.musify.app.presentation.common.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistBottomSheet(
    playlists: MutableList<Playlist>,
    playlistSheetState : SheetState,
    onDismiss: ()->Unit
) {
    ModalBottomSheet(containerColor = AlbumCoverBlackBG,
        sheetState = playlistSheetState,
        onDismissRequest = onDismiss) {
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