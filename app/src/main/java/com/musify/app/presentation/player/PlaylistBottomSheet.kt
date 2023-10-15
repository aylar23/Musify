package com.musify.app.presentation.player

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.musify.app.PlayerController
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.utils.reorder.ReorderableItem
import com.musify.app.ui.utils.reorder.detectReorderAfterLongPress
import com.musify.app.ui.utils.reorder.rememberReorderableLazyListState
import com.musify.app.ui.utils.reorder.reorderable
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    playerController: PlayerController
) {
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        playerController.onReorder(from.index, to.index)
    })


    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {


        items(playerController.tracks, { it.id }) { song ->


            ReorderableItem(state, key = song.id) { isDragging ->
                val elevation =
                    animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    SongView(song = song, reorderable = true, onMoreClicked = {}) {
                        playerController.onTrackClick(song)
                    }
                }

            }
        }
    }




}


