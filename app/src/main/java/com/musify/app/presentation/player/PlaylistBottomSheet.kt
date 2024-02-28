package com.musify.app.presentation.player

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.PlayerController
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.utils.reorder.ReorderableItem
import com.musify.app.ui.utils.reorder.detectReorder
import com.musify.app.ui.utils.reorder.detectReorderAfterLongPress
import com.musify.app.ui.utils.reorder.rememberReorderableLazyListState
import com.musify.app.ui.utils.reorder.reorderable
import com.skydoves.flexible.core.FlexibleSheetState
import java.lang.Exception
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistBottomSheet(
    playerController: PlayerController,
    sheetState: FlexibleSheetState
) {

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        playerController.onReorder(from.index, to.index)
    })


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AlbumCoverBlackBG),
            contentAlignment = Alignment.Center

        ) {
            Surface(
                modifier = Modifier
                    .padding(vertical = 22.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.extraLarge

            ) {
                Box(
                    Modifier
                        .size(
                            width = 32.0.dp,
                            height = 4.0.dp
                        )
                )
            }
        }
        Box() {

            playerController.selectedTrack?.let {
                MiniPlayer(
                    song = it, alpha = 1f
                ) {
                    playerController.onPlayPauseClick()
                }
            }
        }
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .fillMaxHeight()
                .reorderable(state)
                .detectReorderAfterLongPress(state)
                .background(Surface, MaterialTheme.shapes.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {

            itemsIndexed(playerController.tracks, { index, item -> item.songId }) { index, song ->


                ReorderableItem(state, key = song.songId) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                    Column(
                        modifier = Modifier
                            .shadow(elevation.value)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        SongView(
                            modifier = Modifier.background(Surface),
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


}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MiniPlayer(
    song: Song,
    alpha: Float,
    onPlayPauseClick: () -> Unit,
) {


    val songImagePainter = rememberAsyncImagePainter(
        model = song.getSongImage(),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .background(color = AlbumCoverBlackBG)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(DarkGray),
            painter = songImagePainter,
            contentDescription = "artist image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .basicMarquee(
                        // Animate forever.
                        iterations = Int.MAX_VALUE,
                    ),
                text = song.name,
                fontFamily = SFFontFamily,
                fontSize = 18.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.getArtistsName(),
                fontFamily = SFFontFamily,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(
            modifier = Modifier.size(35.dp),
            onClick = onPlayPauseClick,
        ) {
            Icon(
                painter = if (song.isPlaying()) painterResource(id = R.drawable.pause) else painterResource(
                    id = R.drawable.play
                ), contentDescription = "see more", tint = Color.White
            )

        }


    }


}