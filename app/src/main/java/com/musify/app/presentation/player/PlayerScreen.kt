package com.musify.app.presentation.player

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import coil.compose.rememberAsyncImagePainter
import com.musify.app.MainViewModel
import com.musify.app.PlayerController
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.player.PlaybackState
import com.musify.app.player.components.PlayerTopAppBar
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import com.musify.app.ui.utils.formatTime
import com.musify.app.ui.utils.rememberFlowWithLifecycle
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@SuppressLint("UnrememberedMutableState")
@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun PlayerScreen(
    mainViewModel: MainViewModel,
    playerController: PlayerController,
    playerBottomSheet: SheetState,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Long) -> Unit,
    onDismiss: () -> Unit
) {

    var shuffleEnabled by rememberSaveable {
        mutableStateOf(playerController.getShuffleMode())
    }

//  Repeat modes for playback. One of REPEAT_MODE_OFF, REPEAT_MODE_ONE or REPEAT_MODE_ALL.
    var repeatMode by rememberSaveable {
        mutableStateOf(playerController.getRepeatMode())
    }


    val scope = rememberCoroutineScope()
    val coroutineScope = rememberCoroutineScope()

    val playlists = mainViewModel.getAllPlaylists().collectAsState(initial = emptyList())


    var settingsClicked by remember {
        mutableStateOf(false)
    }


    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }


    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    var showPlaylistBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pagerState =
        androidx.compose.foundation.pager.rememberPagerState(playerController.selectedTrackIndex,
            pageCount = { playerController.tracks.size })

    val playlistBottomSheet = rememberFlexibleBottomSheetState(
        skipIntermediatelyExpanded = true,
        skipSlightlyExpanded = true,
        containSystemBars = false,
        allowNestedScroll = false,
        isModal = true,
    )


    LaunchedEffect(playerController.selectedTrackIndex) {

        if (playerController.selectedTrackIndex >= 0 && pagerState.currentPage != playerController.selectedTrackIndex) {

            delay(300)
            if (!playerBottomSheet.isVisible) pagerState.scrollToPage(playerController.selectedTrackIndex) else pagerState.animateScrollToPage(
                playerController.selectedTrackIndex
            )
        }
    }

    LaunchedEffect(pagerState.currentPage) {

        if (playerBottomSheet.isVisible) {
            if (playerController.selectedTrackIndex >= 0 && playerController.selectedTrackIndex < playerController.tracks.size && pagerState.currentPage != playerController.selectedTrackIndex) {
                delay(400)
                playerController.onTrackClick(playerController.tracks[pagerState.currentPage])
            }
        }

    }

//    val localPlaylist = playerController.getAllPlaylists().collectAsState(initial = emptyList<Playlist>()).value
    val playbackStateValue by playerController.playbackState.collectAsState(
        initial = PlaybackState(0L, 0L)
    )

    val progressState by rememberFlowWithLifecycle(playerController.playbackState)
    var draggingProgress by remember { mutableStateOf<Float?>(null) }

    var currentMediaProgress = progressState.currentPlaybackPosition.toFloat()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(id = R.string.successfully_added)

    ModalBottomSheet(onDismissRequest = onDismiss,
        sheetState = playerBottomSheet,
        dragHandle = {}
    ) {


        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        containerColor = Inactive,
                        contentColor = WhiteTextColor,
                        snackbarData = data
                    )
                }
            },
        ) { innerPadding ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .background(AlbumCoverBlackBG)
            ) {
                PlayerTopAppBar(
                    playerController.selectedTrack,
                    onMoreClicked = {
                        settingsClicked = true
                    }
                ) {
                    onDismiss()
                }

                HorizontalPager(
                    pageSpacing = 2.dp,
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    state = pagerState
                ) { page ->

                    Card(shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .graphicsLayer {

                                val pageOffset =
                                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                                alpha = lerp(
                                    start = 0.95f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }

                            }


                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberAsyncImagePainter(
                                model = playerController.tracks[page].getSongImage() ?: ""
                            ),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                    }

                }
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                modifier = Modifier.basicMarquee(
                                    // Animate forever.
                                    iterations = Int.MAX_VALUE,
                                ),
                                text = playerController.selectedTrack?.name ?: "",
                                fontSize = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = SFFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = WhiteTextColor
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = playerController.selectedTrack?.getArtistsName() ?: "",
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 18.sp,
                                fontFamily = SFFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = WhiteTextColor
                            )
                        }
                        IconButton(onClick = {
                            addToPlaylistClicked = true
                        }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                tint = WhiteTextColor,
                                painter = painterResource(id = R.drawable.library_add),
                                contentDescription = null
                            )
                        }
//

                    }


                    Spacer(modifier = Modifier.height(20.dp))

//                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {

                        Slider(
                            value = draggingProgress ?: currentMediaProgress,
                            onValueChange = { value ->
                                draggingProgress = value
                            },
                            onValueChangeFinished = {
                                draggingProgress?.toLong()?.let {
                                    playerController.onSeekBarPositionChanged(it)
                                }
                                draggingProgress = null
                            },

                            valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
                            modifier = Modifier.fillMaxWidth(),

                            colors = SliderDefaults.colors(
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = GrayTextColor,
                            ),
                            thumb = {
                                SliderDefaults.Thumb(
                                    modifier = Modifier
                                        .padding(vertical = 5.dp),
                                    interactionSource = remember { MutableInteractionSource() },
                                    thumbSize = DpSize(14.dp, 14.dp)
                                )
                            },
                            track = {
                                SliderDefaults.Track(modifier = Modifier
                                    .height(5.dp)
                                    .padding(0.dp)
                                    .defaultMinSize(1.dp, 1.dp),
                                    sliderState = it,
                                    thumbTrackGapSize = 0.dp,
                                    drawStopIndicator = {})
                            })
//                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
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


                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        IconButton(modifier = Modifier.weight(.8f), onClick = {
                            playerController.toggleShuffle()

                            shuffleEnabled = playerController.getShuffleMode()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = stringResource(id = R.string.shuffle),
                                tint = if (shuffleEnabled) Yellow else GrayTextColor
                            )
                        }
                        IconButton(
                            modifier = Modifier.weight(.8f),
                            onClick = playerController::onPreviousClick
                        ) {
                            Icon(
                                modifier = Modifier.size(44.dp),
                                painter = painterResource(id = R.drawable.media_skip_backward),
                                contentDescription = stringResource(id = R.string.shuffle),
                                tint = WhiteTextColor
                            )
                        }
                        FloatingActionButton(
                            modifier = Modifier
                                .size(74.dp)
                                .clip(shape = CircleShape),
                            onClick = playerController::onPlayPauseClick,
                            containerColor = Yellow,
                            contentColor = AlbumCoverBlackBG,
                        ) {
                            Icon(
                                modifier = Modifier.size(46.dp),
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
                                modifier = Modifier.size(44.dp),
                                painter = painterResource(id = R.drawable.media_skip_forward),
                                contentDescription = stringResource(id = R.string.shuffle),
                                tint = WhiteTextColor
                            )
                        }
                        val painter = if (repeatMode == REPEAT_MODE_ONE) R.drawable.repeat_one
                        else R.drawable.repeat

                        IconButton(modifier = Modifier.weight(.8f), onClick = {
                            when (playerController.getRepeatMode()) {
                                REPEAT_MODE_OFF -> {
                                    playerController.setRepeatMode(REPEAT_MODE_ALL)
                                }

                                REPEAT_MODE_ALL -> {
                                    playerController.setRepeatMode(REPEAT_MODE_ONE)

                                }

                                REPEAT_MODE_ONE -> {
                                    playerController.setRepeatMode(REPEAT_MODE_OFF)

                                }
                            }

                            repeatMode = playerController.getRepeatMode()
                        }) {
                            Icon(
                                painter = painterResource(id = painter),
                                contentDescription = stringResource(id = R.string.repeat),
                                tint = if (repeatMode != REPEAT_MODE_OFF) Yellow else GrayTextColor
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                tint = WhiteTextColor,
                                painter = painterResource(id = R.drawable.ic_cast),
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }

                        IconButton(
                            onClick = {
                                showPlaylistBottomSheet = true
                            }
                        ) {
                            Icon(
                                tint = WhiteTextColor,
                                painter = painterResource(id = R.drawable.ic_list),
                                contentDescription = stringResource(id = R.string.go_back)
                            )
                        }
                    }


                }


                if (showPlaylistBottomSheet) {
                    FlexibleBottomSheet(
                        onDismissRequest = {
                            showPlaylistBottomSheet = false

                        },
//                dragHandle = {},
                        sheetState = playlistBottomSheet,
//                shape = RoundedCornerShape(0.dp),
                        containerColor = AlbumCoverBlackBG
                    ) {
                        PlaylistBottomSheet(
                            sheetState = playlistBottomSheet, playerController = playerController
                        )
                    }
                }


            }
        }

    }

    if (settingsClicked && playerController.selectedTrack != null) {
        TrackBottomSheet(
            selectedSong = playerController.selectedTrack!!,
            songSettingsSheetState = songSettingsSheetState,
            onAddToPlaylist = {
                scope.launch {
                    songSettingsSheetState.hide()
                    settingsClicked = false

                }
                addToPlaylistClicked = true
            },
            onNavigateToAlbum = {
                playerController.selectedTrack?.albumId?.let { navigateToAlbum(it) }
            },
            onNavigateToArtist = {
                if (playerController.selectedTrack?.artists?.size == 1) {
                    playerController.selectedTrack?.getArtist()?.let { navigateToArtist(it) }
                } else {
                    showArtistDialog = true
                }
            },
            onPlayNext = {
                playerController.selectedTrack?.let {
                    playerController.onPlayNext(
                        it
                    )
                }
            },
            onShare = {},
        ) {
            settingsClicked = false
        }
    }



    if (addToPlaylistClicked && playerController.selectedTrack != null) {
        AddToPlaylistBottomSheet(
            selectedSong = playerController.selectedTrack!!,
            playlists = playlists.value,
            playlistSheetState = playlistSheetState,
            onCreateNewPlaylist = {
                showNewPlaylistDialog = true
            },
            onSelect = { playlist ->
                playerController.selectedTrack?.let {
                    mainViewModel.addSongToPlaylist(it, playlist)

                }
                scope.launch {
                    snackbarHostState.showSnackbar(snackbarMessage)
                }
            }) {
            addToPlaylistClicked = false
        }
    }



    if (showNewPlaylistDialog) {
        Dialog(onDismissRequest = { showNewPlaylistDialog = false }) {
            NewPlaylistDialog() { name ->
                showNewPlaylistDialog = false
                mainViewModel.addNewPlaylist(name)
                scope.launch {
                    snackbarHostState.showSnackbar(snackbarMessage)
                }
            }
        }

    }

    if (showArtistDialog && playerController.selectedTrack != null) {
        ArtistBottomSheet(
            selectedSong = playerController.selectedTrack!!,
            artists = playerController.selectedTrack?.artists ?: emptyList(),
            sheetState = artistsSheetState,
            onSelect = { artist -> navigateToArtist(artist) },
            onDismiss = { showArtistDialog = false }

        )

    }

}