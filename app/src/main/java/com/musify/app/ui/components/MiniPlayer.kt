package com.musify.app.ui.components

import android.provider.SyncStateContract.Columns
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.PlayerController
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.player.PlaybackState
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MiniPlayer(
    song: Song,
    playerController: PlayerController,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {


    val songImagePainter = rememberAsyncImagePainter(
        model = song.getSongImage(),
    )
    val playbackStateValue = playerController.playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    ).value
    var currentMediaProgress = playbackStateValue.currentPlaybackPosition.toFloat()
    var currentPosTemp by rememberSaveable { mutableStateOf(0f) }

    Column() {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
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
                modifier = Modifier
                    .size(35.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Yellow
                ),
                onClick = onPlayPauseClick,
            ) {
                Icon(
                    painter = if (song.isPlaying()) painterResource(id = R.drawable.pause) else painterResource(
                        id = R.drawable.play
                    ),
                    contentDescription = "see more",
                    tint = Black
                )

            }

        }
        val interactionSource = remember { MutableInteractionSource() }
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Slider(
                value = if (currentPosTemp == 0f) currentMediaProgress else currentPosTemp,
                onValueChange = {},
                onValueChangeFinished = {},
                enabled = false,
                valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
                modifier = Modifier
                    .padding(0.dp)
                    .height(0.dp)
                    .background(Color.Red)
                    .defaultMinSize(1.dp, 5.dp)
                    .fillMaxWidth(),
                colors = SliderColors(
                    thumbColor = WhiteTextColor,
                    activeTickColor = Yellow,
                    activeTrackColor = Yellow,
                    inactiveTickColor = GrayTextColor,
                    inactiveTrackColor = GrayTextColor,
                    disabledThumbColor = GrayTextColor,
                    disabledActiveTrackColor = Yellow,
                    disabledActiveTickColor = Yellow,
                    disabledInactiveTrackColor = GrayTextColor,
                    disabledInactiveTickColor = GrayTextColor
                ),

                thumb = {
//                    SliderDefaults.Thumb(
//                        modifier = Modifier,
//                        interactionSource = interactionSource,
//                        colors = SliderDefaults.colors(),
//                        thumbSize = DpSize(1.dp, 1.dp)
//                    )
                },

                track = {
                    SliderDefaults.Track(
                        sliderState = it,
                        modifier = Modifier
                            .padding(0.dp)
                            .defaultMinSize(1.dp, 1.dp)


                    )
                }

            )
        }

    }
}