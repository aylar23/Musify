package com.musify.app.ui.components.bottomsheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.musify.app.R
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.theme.AlbumCoverBlackBG


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackBottomSheet(
    songSettingsSheetState: SheetState,
    onAddToPlaylist: () -> Unit,
    onPlayNext: () -> Unit,
    onNavigateToArtist: () -> Unit,
    onNavigateToAlbum: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = songSettingsSheetState,
        onDismissRequest = onDismiss
    ) {


        ActionsModelView(
            expandable = true,
            icon = R.drawable.library_add,
            text = stringResource(id = R.string.add_to_playlist),
            playlist = null
        ) {
            onAddToPlaylist()

        }


        ActionsModelView(
            expandable = false,
            icon = R.drawable.redo,
            text = stringResource(id = R.string.play_next),
            playlist = null
        ) {
            onPlayNext()
        }
        ActionsModelView(
            expandable = true,
            icon = R.drawable.account_circle,
            text = stringResource(id = R.string.see_the_artist),
            playlist = null
        ) {
            onNavigateToArtist()
        }
        ActionsModelView(
            expandable = true,
            icon = R.drawable.album,
            text = stringResource(id = R.string.see_the_album),
            playlist = null
        ) {
            onNavigateToAlbum()
        }
        ActionsModelView(
            expandable = false,
            icon = R.drawable.share,
            text = stringResource(id = R.string.share),
            playlist = null
        ) {
            onShare()

        }

    }


}