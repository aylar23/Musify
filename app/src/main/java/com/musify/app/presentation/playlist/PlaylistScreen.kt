package com.musify.app.presentation.playlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.SwipeableSongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.toolbar.CollapsingToolbarScaffold
import com.musify.app.ui.components.toolbar.ScrollStrategy
import com.musify.app.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    id: Long,
    type: String,
    paddingValues: PaddingValues,
    playlistViewModel: PlaylistViewModel,
    navigateToNewPlaylist: () -> Unit,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Long) -> Unit,
    navigateUp: () -> Unit,
) {

    LaunchedEffect(id, type) {
        playlistViewModel.setPlaylistIdAndType(id, type)
    }

    lateinit var selectedSong: Song

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }
    val playlists by playlistViewModel.getAllPlaylists().collectAsState(initial = emptyList())
    val playlistExists by playlistViewModel.playlistExists().collectAsState(initial = false)

    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val uiState by playlistViewModel.uiState.collectAsState()

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val state = rememberCollapsingToolbarScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage = stringResource(id = R.string.successfully_added)

    Scaffold(
        modifier = Modifier
            .padding(paddingValues),
        snackbarHost = { SnackbarHost(snackbarHostState) { data ->
            Snackbar(
                containerColor = Inactive,
                contentColor = WhiteTextColor,
                snackbarData = data
            )
        } },
    ) { _ ->


        CollapsingToolbarScaffold(
            modifier = Modifier,
            state = state,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {

                val textSize = (22 + (30 - 16) * state.toolbarState.progress).sp
                val padding = (44 * (1 - state.toolbarState.progress)).dp

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .pin()
                        .background(color = MaterialTheme.colorScheme.background)
                )


                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .parallax(0.5f)
                        .graphicsLayer {
                            // change alpha of Image as the toolbar expands
                            alpha = state.toolbarState.progress
                        },
                    painter = rememberAsyncImagePainter(model = uiState.data?.getPlaylistImage()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = if (textSize.value == 22f) 0f else 1f
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    TransparentColor,
                                    Background
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .padding(padding, 8.dp, 16.dp, 8.dp)
                        .road(
                            whenCollapsed = Alignment.TopStart,
                            whenExpanded = Alignment.BottomStart
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)

                ) {
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                            .weight(1f),
                        text = uiState.data?.name ?: "",
                        style = TextStyle(color = White, fontSize = textSize),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )



                    if (!playlistExists) {
                        IconButton(
                            onClick = {
                                uiState.data?.let { playlistViewModel.savePlaylist(it) }
                                scope.launch {
                                    snackbarHostState.showSnackbar(snackbarMessage)
                                }
                            },
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(id = R.drawable.library_add),
                                tint = WhiteTextColor,
                                contentDescription = ""
                            )
                        }
                    }

                }

                IconButton(
                    modifier = Modifier
                        .pin()
                        .padding(vertical = 8.dp),
                    onClick = { navigateUp() }) {
                    Icon(
                        tint = WhiteTextColor,
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = stringResource(id = R.string.go_back)
                    )
                }


            }
        ) {


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = R.string.play_all,
                            onClick = {
                                uiState.data?.songs?.let { data ->

                                    if (data.isNotEmpty()) {
                                        playlistViewModel.getPlayerController().init(data[0], data)
                                    }

                                }

                            },
                            containerColor = Inactive,
                            contentColor = Yellow,
                            leadingIcon = R.drawable.play,
                            shape = MaterialTheme.shapes.medium


                        )

                        Spacer(modifier = Modifier.weight(.1f))

                        CustomButton(
                            modifier = Modifier.weight(1f),
                            text = R.string.shuffle,
                            onClick = {
                                uiState.data?.songs?.let { data ->

                                    if (data.isNotEmpty()) {
                                        playlistViewModel.getPlayerController()
                                            .init(data[0], data)
                                    }

                                }
                            },
                            containerColor = Inactive,
                            contentColor = Yellow,
                            leadingIcon = R.drawable.shuffle,
                            shape = MaterialTheme.shapes.medium

                        )


                    }
                }
                when {
                    uiState.isLoading -> {
                        item {

                            LoadingView(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(Background)
                            )
                        }
                    }

                    uiState.isFailure -> {
                        item {

                            NetworkErrorView(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(Background)
                            ) {
                                playlistViewModel.getPlaylist(id, type)
                            }
                        }
                    }

                    uiState.isSuccess -> {
                        uiState.data?.songs?.let { data ->
                            items(
                                data,
                            ) { song ->

                                SwipeableSongView(song = song,
                                    playerController = playlistViewModel.getPlayerController(),
                                    onMoreClicked = {
                                        selectedSong = it
                                        settingsClicked = true

                                    },
                                    downloadTracker = playlistViewModel.getDownloadTracker(),

                                    onSwipe = {
                                        playlistViewModel.getPlayerController().onPlayNext(song)
                                    }
                                ) {
                                    playlistViewModel.getPlayerController().init(song, data)

                                }
                            }
                        }

                    }

                }
            }

            if (settingsClicked) {
                TrackBottomSheet(
                    selectedSong = selectedSong,
                    songSettingsSheetState = songSettingsSheetState,
                    onAddToPlaylist = {
                        settingsClicked = false
                        addToPlaylistClicked = true
                    },
                    onNavigateToAlbum = {
                        selectedSong.albumId?.let { navigateToAlbum(it) }
                    },
                    onNavigateToArtist = {
                        if(selectedSong.artists.size == 1){
                            navigateToArtist(selectedSong.getArtist())
                        }else{
                            showArtistDialog = true
                        }
                    },
                    onPlayNext = {
                        playlistViewModel.getPlayerController().selectedTrack?.let {
                            playlistViewModel.getPlayerController().onPlayNext(
                                it
                            )
                        }
                    },
                    onShare = {},
                ) {
                    settingsClicked = false
                }
            }




            if (addToPlaylistClicked) {
                AddToPlaylistBottomSheet(
                    playlists = playlists,
                    playlistSheetState = playlistSheetState,
                    onCreateNewPlaylist = {
                        showNewPlaylistDialog = true
                    },
                    onSelect = { playlist ->
                        playlistViewModel.addSongToPlaylist(selectedSong, playlist)
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarMessage)
                        }
                    }
                ) {

                    addToPlaylistClicked = false
                }
            }

            if (showNewPlaylistDialog) {
                Dialog(onDismissRequest = { showNewPlaylistDialog = false }) {
                    NewPlaylistDialog() { name ->
                        showNewPlaylistDialog = false
                        playlistViewModel.addNewPlaylist(name)
                        scope.launch {
                            snackbarHostState.showSnackbar(snackbarMessage)
                        }
                    }
                }

            }

            if (showArtistDialog) {
                ArtistBottomSheet(
                    artists = selectedSong.artists,
                    sheetState = artistsSheetState,
                    onSelect = { artist-> navigateToArtist(artist) },
                    onDismiss = { showArtistDialog = false}

                )

            }

        }

    }
}
