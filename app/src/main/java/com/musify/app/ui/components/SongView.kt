package com.musify.app.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.common.base.Preconditions
import com.musify.app.PlayerController
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.player.DownloadTracker
import com.musify.app.player.PlayerStates
import com.musify.app.ui.components.swipe.SwipeAction
import com.musify.app.ui.components.swipe.SwipeableActionsBox
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun SongView(
    modifier: Modifier? = null,
    playerController: PlayerController,
    song: Song,
    reorderable: Boolean = false,
    downloadTracker: DownloadTracker? = null,
    onMoreClicked: (Song) -> Unit,
    onClick: () -> Unit,
) {


    val isDownloaded = downloadTracker?.isDownloaded(song.toMediaItem()) ?: false
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    Row(modifier = modifier?.clickable { onClick() }?.padding(5.dp)
        ?: Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically) {
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

        Row {
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (song == playerController.selectedTrack) {
                        LottieAnimation(
                            composition,
                            progress,
                            modifier = Modifier.size(20.dp)
                        )
                    }

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
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    if (isDownloaded) {
                        Icon(
                            modifier = Modifier.padding(end = 2.dp),
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = "song setting",
                            tint = Color.Unspecified
                        )
                    }
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


    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableSongView(
    @SuppressLint("ModifierParameter") modifier: Modifier? = null,
    playerController: PlayerController,
    song: Song,
    reorderable: Boolean = false,
    downloadTracker: DownloadTracker? = null,
    onMoreClicked: (Song) -> Unit,
    onSwipe: () -> Unit,
    onClick: () -> Unit,
) {
    val playNext = SwipeAction(
        icon = painterResource(id = R.drawable.redo),
        background = MaterialTheme.colorScheme.primary,
        isUndo = false,
        onSwipe = onSwipe,
    )

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.equalizer)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    val isDownloaded = downloadTracker?.isDownloaded(song.toMediaItem()) ?: false
    val isDownloading = downloadTracker?.isDownloading(song.toMediaItem()) ?: false

    val endActions = listOf(playNext)

    SwipeableActionsBox(
        endActions = endActions, swipeThreshold = 40.dp
    ) {
        // Swipeable content goes here.
        Row(modifier = modifier?.clickable { onClick() } ?: Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Box(modifier = Modifier.width(IntrinsicSize.Min)) {

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

                if (isDownloading) {


                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .size(50.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(25.dp),
                            strokeWidth = 1.dp
                        )
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            painter = painterResource(id = R.drawable.ic_download),
                            contentDescription = "song setting",
                            tint = Color.Unspecified
                        )

                    }


                }
            }


            Row {
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        if (song == playerController.selectedTrack) {
                            LottieAnimation(
                                composition,
                                progress,
                                modifier = Modifier.size(20.dp)
                            )
                        }

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
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isDownloaded) {
                            Icon(
                                modifier = Modifier.padding(end = 2.dp),
                                painter = painterResource(id = R.drawable.ic_check_circle),
                                contentDescription = "song setting",
                                tint = Color.Unspecified
                            )
                        }
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


        }

    }
}

@androidx.annotation.OptIn(UnstableApi::class)
fun MediaItem.isDownloaded(downloads: State<Map<Uri, Download>>?): Boolean {

    if (downloads == null) return false
    val download = downloads.value[Preconditions.checkNotNull(localConfiguration).uri]
    return download != null && download.state != Download.STATE_FAILED
}
