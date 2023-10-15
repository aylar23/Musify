package com.musify.app.presentation.player

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.musify.app.PlayerController
import com.musify.app.R
import com.musify.app.player.PlaybackState
import com.musify.app.player.PlayerStates
import com.musify.app.ui.components.ActionsModelView
import com.musify.app.ui.components.CollapsingSmallTopAppBar
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import com.musify.app.ui.utils.formatTime
import com.musify.app.ui.utils.reorder.ReorderableItem
import com.musify.app.ui.utils.reorder.detectReorderAfterLongPress
import com.musify.app.ui.utils.reorder.rememberReorderableLazyListState
import com.musify.app.ui.utils.reorder.reorderable
import kotlin.math.absoluteValue


@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalPagerApi::class
)
@Composable

fun PlayerScreen(
    playerController: PlayerController, playerBottomSheet: SheetState, onDismiss: () -> Unit
) {
    val pagerState = rememberPagerState()

    var shuffleEnabled by rememberSaveable {
        mutableStateOf(false)
    }
    var repeatEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(playerController.selectedTrackIndex) {

        if (playerController.selectedTrackIndex >= 0 && pagerState.currentPage != playerController.selectedTrackIndex) {

            pagerState.animateScrollToPage(playerController.selectedTrackIndex)

        }
    }

    LaunchedEffect(pagerState.currentPage) {


        if (!playerBottomSheet.isVisible)return@LaunchedEffect
        if (playerController.selectedTrackIndex >= 0 && pagerState.currentPage != playerController.selectedTrackIndex) {
            playerController.onTrackClick(pagerState.currentPage)
        }
    }

    val playbackStateValue = playerController.playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    ).value
    var currentMediaProgress = playbackStateValue.currentPlaybackPosition.toFloat()
    var currentPosTemp by rememberSaveable { mutableStateOf(0f) }


    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(scaffoldState = scaffoldState, sheetPeekHeight = 128.dp, sheetContent = {
        PlaylistBottomSheet(playerController = playerController)

    }, content = { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AlbumCoverBlackBG)
        ) {
            CollapsingSmallTopAppBar(
                scrollBehaviour = null,
                trailingIcon = R.drawable.menu,
                trailingIconDescription = stringResource(id = R.string.menu)
            ) {
                onDismiss()
            }

            HorizontalPager(
                itemSpacing = 1.dp,
                count = playerController.tracks.size,
                contentPadding = PaddingValues(horizontal = 60.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.6f),
                state = pagerState
            ) { page ->
                Card(
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier.graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.80f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                    },

                    ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = playerController.tracks[page].getSongImage() ?: ""
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                ) {
                    ActionsModelView(
                        mainText = playerController.selectedTrack?.name ?: "",
                        grayText = playerController.selectedTrack?.getArtistsName() ?: "",
                        expandable = true,
                        trailingIcon = R.drawable.add,
                        paddingValues = 0
                    ) {

                    }
                }


                Slider(
                    value = if (currentPosTemp == 0f) currentMediaProgress else currentPosTemp,
                    onValueChange = { currentPosTemp = it },
                    onValueChangeFinished = {
                        currentMediaProgress = currentPosTemp
                        currentPosTemp = 0f
                        playerController.onSeekBarPositionChanged(currentMediaProgress.toLong())
                    },
                    valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                )

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = playbackStateValue.currentPlaybackPosition.formatTime(),
                        fontSize = 14.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = WhiteTextColor
                    )
                    Text(
                        modifier = Modifier,
                        text = playbackStateValue.currentTrackDuration.formatTime(),
                        fontSize = 14.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = WhiteTextColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50.dp))
                        .background(DarkGray)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.playing_on_device_ic),
                        contentDescription = "",
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = stringResource(id = R.string.playing_on) + "iPhone",
                        fontSize = 14.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight.Thin,
                        color = WhiteTextColor
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(modifier = Modifier.weight(.8f), onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.shuffle),
                            contentDescription = stringResource(id = R.string.shuffle),
                            tint = if (shuffleEnabled) WhiteTextColor else GrayTextColor
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(.8f), onClick = playerController::onPreviousClick
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(id = R.drawable.media_skip_backward),
                            contentDescription = stringResource(id = R.string.shuffle),
                            tint = WhiteTextColor
                        )
                    }
                    FloatingActionButton(
                        modifier = Modifier.clip(shape = CircleShape),
                        onClick = playerController::onPlayPauseClick,
                        containerColor = Yellow,
                        contentColor = AlbumCoverBlackBG,
                    ) {
                        Icon(
                            modifier = Modifier.padding(20.dp),
                            painter = if (playerController.selectedTrack?.isPlaying() == true) painterResource(
                                id = R.drawable.pause
                            ) else painterResource(id = R.drawable.play),
                            contentDescription = stringResource(id = R.string.pause),
                            tint = Background
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(.8f), onClick = playerController::onNextClick
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = painterResource(id = R.drawable.media_skip_forward),
                            contentDescription = stringResource(id = R.string.shuffle),
                            tint = WhiteTextColor
                        )
                    }
                    IconButton(modifier = Modifier.weight(.8f), onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.repeat),
                            contentDescription = stringResource(id = R.string.repeat),
                            tint = if (shuffleEnabled) WhiteTextColor else GrayTextColor
                        )
                    }

                }

            }


        }


    })


}