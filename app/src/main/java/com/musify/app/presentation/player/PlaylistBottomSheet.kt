package com.musify.app.presentation.player

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.musify.app.PlayerController
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.utils.reorder.ReorderableItem
import com.musify.app.ui.utils.reorder.detectReorderAfterLongPress
import com.musify.app.ui.utils.reorder.rememberReorderableLazyListState
import com.musify.app.ui.utils.reorder.reorderable


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
            .fillMaxHeight()
            .reorderable(state)
            .detectReorderAfterLongPress(state)
            .background(Surface),
    ) {


        items(playerController.tracks, { it.songId }) { song ->


            ReorderableItem(state, key = song.songId) { isDragging ->
                val elevation =
                    animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    SongView(
                        modifier = Modifier
                            .background(Surface)
                        ,
                        song = song,
                        reorderable = true,
                        onMoreClicked = {}) {
                        playerController.onTrackClick(song)
                    }
                }

            }
        }
    }


}


