package com.musify.app.ui.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SurfaceSecond


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackBottomSheet(
    deletable: Boolean = false,
    selectedSong: Song,
    songSettingsSheetState: SheetState,
    onAddToPlaylist: () -> Unit,
    onPlayNext: () -> Unit,
    onNavigateToArtist: () -> Unit,
    onNavigateToAlbum: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit
) {


    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = songSettingsSheetState,
        dragHandle = {},
        onDismissRequest = onDismiss,
        windowInsets = WindowInsets(0),

        ) {


        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
        ) {

            ActionsModelView(
                expandable = true,
                icon = R.drawable.library_add,
                mainText = stringResource(id = R.string.add_to_playlist)
            ) {
                onDismiss()
                onAddToPlaylist()

            }


            ActionsModelView(
                expandable = false,
                icon = R.drawable.redo,
                mainText = stringResource(id = R.string.play_next)
            ) {
                onDismiss()
                onPlayNext()
            }


            ActionsModelView(
                expandable = true,
                icon = R.drawable.account_circle,
                mainText = stringResource(id = R.string.see_the_artist)
            ) {
                onDismiss()
                onNavigateToArtist()
            }


            if (selectedSong.albumId != null ){
                ActionsModelView(
                    expandable = true,
                    icon = R.drawable.album,
                    mainText = stringResource(id = R.string.see_the_album)
                ) {
                    onDismiss()
                    onNavigateToAlbum()
                }
            }

            if (deletable){
                ActionsModelView(
                    expandable = false,
                    icon = R.drawable.ic_trash,
                    mainText = stringResource(id = R.string.delete_from_playlist)
                ) {
                    onDelete()
                    onDismiss()

                }
            }

            ActionsModelView(
                expandable = false,
                icon = R.drawable.share,
                mainText = stringResource(id = R.string.share)
            ) {
                onDismiss()
                onShare()

            }

            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                text = R.string.cancel,
                onClick = onDismiss,
                containerColor = SurfaceSecond,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.small
            )
        }

        Spacer(Modifier.navigationBarsPadding())


    }
}


