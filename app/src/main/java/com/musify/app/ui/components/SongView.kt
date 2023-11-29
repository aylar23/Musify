package com.musify.app.ui.components

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDragScope
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.swipe.SwipeAction
import com.musify.app.ui.components.swipe.SwipeableActionsBox
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongView(
    @SuppressLint("ModifierParameter") modifier: Modifier? = null,
    song: Song,
    reorderable: Boolean = false,
    onMoreClicked: (Song) -> Unit,
    onClick: () -> Unit,
) {

    Row(
        modifier = modifier?.clickable { onClick() }?.padding(5.dp) ?: Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .aspectRatio(1f)
                .background(Surface),
            model = song.getSongImage(),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            alignment = Alignment.Center

        )

        Column {
            Row {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = song.name,
                        fontFamily = SFFontFamily,
                        fontSize = 16.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = WhiteTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = song.getArtistsName(),
                        fontFamily = SFFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = GrayTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (reorderable) {

                    Icon(
                        modifier = Modifier.padding(20.dp),
                        painter = painterResource(id = R.drawable.two_lines),
                        contentDescription = "song setting",
                        tint = WhiteTextColor
                    )

                } else {
                    IconButton(
                        onClick = { onMoreClicked(song) },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.song_setting),
                            contentDescription = "song setting",
                            tint = WhiteTextColor
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(end = 10.dp),
                color = Surface,
                thickness = 0.5.dp
            )
        }


    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableSongView(
    @SuppressLint("ModifierParameter") modifier: Modifier? = null,
    song: Song,
    reorderable: Boolean = false,
    onMoreClicked: (Song) -> Unit,
    onClick: () -> Unit,
) {
    val playNext = SwipeAction(
        icon = painterResource(id = R.drawable.redo),
        background = MaterialTheme.colorScheme.primary,
        isUndo = false,
        onSwipe = { },
    )

    val endActions = listOf(playNext)

    SwipeableActionsBox(
        endActions = endActions,
        swipeThreshold = 40.dp
    ) {
        // Swipeable content goes here.
        Row(
            modifier = modifier?.clickable { onClick() }?.padding(5.dp) ?: Modifier
                .clickable { onClick() }
                .padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = MaterialTheme.shapes.small)
                    .aspectRatio(1f)
                    .background(Surface),
                model = song.getSongImage(),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                alignment = Alignment.Center

            )

            Column {
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = song.name,
                            fontFamily = SFFontFamily,
                            fontSize = 16.sp,
                            lineHeight = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhiteTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = song.getArtistsName(),
                            fontFamily = SFFontFamily,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = GrayTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (reorderable) {

                        Icon(
                            modifier = Modifier.padding(20.dp),
                            painter = painterResource(id = R.drawable.two_lines),
                            contentDescription = "song setting",
                            tint = WhiteTextColor
                        )

                    } else {
                        IconButton(
                            onClick = { onMoreClicked(song) },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.song_setting),
                                contentDescription = "song setting",
                                tint = WhiteTextColor
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(end = 10.dp),
                    color = Surface,
                    thickness = 0.5.dp
                )
            }


        }

    }
}
