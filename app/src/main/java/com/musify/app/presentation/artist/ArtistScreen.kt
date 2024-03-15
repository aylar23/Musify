package com.musify.app.presentation.artist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.OverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.artist.components.LatestReleaseView
import com.musify.app.presentation.player.NewPlaylistDialog
import com.musify.app.presentation.playlist.components.CollapsingTopAppBar
import com.musify.app.ui.components.HeaderView
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.SwipeableSongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.ArtistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.ui.components.toolbar.CollapsingToolbarScaffold
import com.musify.app.ui.components.toolbar.ScrollStrategy
import com.musify.app.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistScreen(
    id: Long,
    paddingValues: PaddingValues,
    artistViewModel: ArtistViewModel,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbums: (Artist) -> Unit,
    navigateToTops: (Artist) -> Unit,
    navigateToSingles: (Artist) -> Unit,
    navigateToAlbum: (Long) -> Unit,
    navigateUp: () -> Unit,
) {


    LaunchedEffect(id) {
        artistViewModel.setID(id)

    }


    val uiState by artistViewModel.uiState.collectAsState()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    val playlists by artistViewModel.getAllPlaylists().collectAsState(initial = emptyList())
    var showNewPlaylistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showArtistDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }


    lateinit var selectedSong: Song
    val playlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val artistsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val songSettingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val density = LocalDensity.current
    val statusBarTop = WindowInsets.statusBars.getTop(density)


    val toolbarHeight = 100.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }

    // our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value =
                    newOffset.coerceIn(-(2 * statusBarTop + toolbarHeightPx), 0f)
                return Offset.Zero
            }
        }
    }
    val state = rememberCollapsingToolbarScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val snackbarMessage = stringResource(id = R.string.successfully_added)

    Scaffold(
        modifier = Modifier
            .padding(paddingValues),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = Inactive,
                    contentColor = WhiteTextColor,
                    snackbarData = data
                )
            }
        },
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
                    painter = rememberAsyncImagePainter(model = uiState.data?.getArtistImage()),
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
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .alpha(state.toolbarState.progress),
                        text = uiState.data?.name ?: "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Row(
                    modifier = Modifier
                        .pin()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center

                ) {

                    IconButton(
                        modifier = Modifier,
                        onClick = { navigateUp() }) {
                        Icon(
                            tint = WhiteTextColor,
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }

                    Text(
                        modifier = Modifier.alpha(1 - state.toolbarState.progress),
                        text = uiState.data?.name ?: "",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )


                }


            }


        ) {


            LazyColumn(
                modifier = Modifier,
            ) {


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
                                artistViewModel.getArtistDetail(id)
                            }
                        }
                    }

                    uiState.isSuccess -> {

                        uiState.data?.let { data ->

                            item {
                                if (data.hasLatestRelease()) {
                                    LatestReleaseView(
                                        latestRelease = data.latestRelease,
                                        navigateToAlbum = { album ->
                                            navigateToAlbum(album.playlistId)
                                        },
                                        playSong = { song ->
                                            artistViewModel.getPlayerController()
                                                .init(song, listOf(song))
                                        }
                                    )
                                }

                            }

                            item {
                                if (data.songs.isNotEmpty()) {
                                    HeaderView(
                                        modifier = Modifier.padding(
                                            top = 20.dp,
                                            bottom = 10.dp
                                        ),
                                        mainText = stringResource(id = R.string.top_songs),
                                        expandable = true
                                    ) {
                                        navigateToTops(data)
                                    }

                                }

                            }
                            items(data.songs) { song ->
                                SwipeableSongView(
                                    song = song,
                                    playerController = artistViewModel.getPlayerController(),
                                    onMoreClicked = {
                                        selectedSong = it
                                        settingsClicked = true
                                    },
                                    downloadTracker = artistViewModel.getDownloadTracker(),

                                    onSwipe = {
                                        artistViewModel.getPlayerController().onPlayNext(song)
                                    }
                                ) {
                                    artistViewModel.getPlayerController().init(song, data.songs)
                                }

                            }


                            item {
                                Box(
                                    modifier = Modifier.padding(top = 20.dp),
                                ) {
                                    AlbumListView(
                                        playlists = data.albums,
                                        expandable = true,
                                        navigateToAlbums = { navigateToAlbums(data) }
                                    ) { album ->
                                        navigateToAlbum(album.playlistId)
                                    }
                                }

                            }
                            item {
                                if (data.singles.isNotEmpty()) {
                                    HeaderView(
                                        modifier = Modifier.padding(
                                            top = 20.dp,
                                            bottom = 10.dp
                                        ),
                                        mainText = stringResource(id = R.string.singles),
                                        expandable = true
                                    ) {
                                        navigateToSingles(data)
                                    }
                                }

                            }
                            items(data.singles) { song ->
                                SwipeableSongView(
                                    song = song,
                                    onMoreClicked = {
                                        selectedSong = it
                                        settingsClicked = true
                                    },
                                    playerController = artistViewModel.getPlayerController(),
                                    downloadTracker = artistViewModel.getDownloadTracker(),

                                    onSwipe = {
                                        artistViewModel.getPlayerController().onPlayNext(song)
                                    }
                                ) {
                                    artistViewModel.getPlayerController()
                                        .init(song, data.singles)
                                }

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
                    if (selectedSong.artists.size == 1) {
                        selectedSong.getArtist().let { navigateToArtist(it) }
                    } else {
                        showArtistDialog = true
                    }
                },
                onPlayNext = {
                    artistViewModel.getPlayerController().selectedTrack?.let {
                        artistViewModel.getPlayerController().onPlayNext(
                            it
                        )
                    }
                },
                onShare = {},
                onDelete = {},
            ) {
                settingsClicked = false
            }
        }


        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(
                selectedSong = selectedSong,
                playlists = playlists,
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    showNewPlaylistDialog = true
                },
                onSelect = { playlist ->
                    artistViewModel.addSongToPlaylist(selectedSong, playlist)
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
                    artistViewModel.addNewPlaylist(name)
                    scope.launch {
                        snackbarHostState.showSnackbar(snackbarMessage)
                    }
                }
            }

        }

        if (showArtistDialog) {
            ArtistBottomSheet(
                selectedSong = selectedSong,
                artists = selectedSong.artists,
                sheetState = artistsSheetState,
                onSelect = { artist -> navigateToArtist(artist) },
                onDismiss = { showArtistDialog = false }

            )

        }
    }


}

