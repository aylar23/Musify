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
            mainText = stringResource(id = R.string.add_to_playlist)
        ) {
            onAddToPlaylist()

        }


        ActionsModelView(
            expandable = false,
            icon = R.drawable.redo,
            mainText = stringResource(id = R.string.play_next)
        ) {
            onPlayNext()
        }
        ActionsModelView(
            expandable = true,
            icon = R.drawable.account_circle,
            mainText = stringResource(id = R.string.see_the_artist)
        ) {
            onNavigateToArtist()
        }
        ActionsModelView(
            expandable = true,
            icon = R.drawable.album,
            mainText = stringResource(id = R.string.see_the_album)
        ) {
            onNavigateToAlbum()
        }
        ActionsModelView(
            expandable = false,
            icon = R.drawable.share,
            mainText = stringResource(id = R.string.share)
        ) {
            onShare()

        }

    }


}