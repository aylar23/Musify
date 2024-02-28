package com.musify.app.ui.components.bottomsheet

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.presentation.localplaylist.LocalPlaylistViewModel
import com.musify.app.presentation.myplaylist.MyPlaylistsViewModel
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LocalPlayListView
import com.musify.app.ui.components.LocalPlaylistSelectionView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Divider
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToPlaylistBottomSheet(
    playlists: List<Playlist>,
    playlistSheetState: SheetState,
    onCreateNewPlaylist: () -> Unit,
    onSelect: (Playlist) -> Unit,
    onDismiss: () -> Unit
) {


    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = playlistSheetState,
        onDismissRequest = onDismiss,
        dragHandle = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            ) {
            LazyColumn(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)

            ) {


                items(playlists) { playlist ->

                    LocalPlaylistSelectionView(
                        playlist = playlist
                    ) {
                        onSelect(playlist)
                        onDismiss()
                    }
                }


            }

            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            startY = 0f, endY = 250f, colors = listOf(
                                TransparentColor, AlbumCoverBlackBG
                            )
                        )
                    )
            )

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = R.string.create_new_playlist,
                onClick = { onCreateNewPlaylist() },
                containerColor = Yellow,
                contentColor = AlbumCoverBlackBG
            )
        }


    }
}