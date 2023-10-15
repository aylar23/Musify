package com.musify.app.presentation.artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.presentation.playlist.components.CollapsingTopAppBar
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.listview.AlbumListView
import com.musify.app.ui.components.listview.SongListView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    id: Long,
    paddingValues: PaddingValues,
    artistViewModel: ArtistViewModel,
    navigateToArtist: (Artist) -> Unit,
    navigateToAlbum: (Playlist) -> Unit,
    navigateToNewPlaylist: () -> Unit,
    navigateUp: () -> Unit,
) {


    LaunchedEffect(id) {
        artistViewModel.getArtistDetail(id)

    }

    val uiState by artistViewModel.uiState.collectAsState()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    lateinit var selectedSong: Song
    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()
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
    Scaffold(modifier = Modifier.padding(paddingValues = paddingValues), topBar = {
        CollapsingTopAppBar(
            title = uiState.data?.name ?: "", scrollBehaviour = scrollBehavior
        ) {
            navigateUp()
        }
    }) { padding ->


        Box(modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .graphicsLayer {
                translationY = scrollBehavior.state.contentOffset
            }) {
            Image(
                painter = rememberAsyncImagePainter(model = uiState.data?.getArtistImage()),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )
        }



        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            item {
                Column(
                    modifier = Modifier.background(
                        brush = Brush.verticalGradient(
                            startY = 0f, endY = 240f, colors = listOf(
                                TransparentColor, AlbumCoverBlackBG
                            )
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp)
                            .padding(vertical = 14.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.data?.name ?: "",
                            color = WhiteTextColor,
                            fontSize = 24.sp,
                            fontFamily = SFFontFamily,

                            fontWeight = FontWeight.Bold
                        )

                    }
                }
            }

            item {

                when {
                    uiState.isLoading -> {
                        LoadingView(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Background)
                        )
                    }

                    uiState.isFailure -> {
                        NetworkErrorView(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(Background)
                        ) {
                            artistViewModel.getArtistDetail(id)
                        }
                    }

                    uiState.isSuccess -> {

                        uiState.data?.let { data ->


                            SongListView(data.songs, onMoreClicked = {
                                selectedSong = it
                                settingsClicked = true
                            }) { song ->

                                artistViewModel.getPlayerController().init(song, data.songs)

                            }

                            AlbumListView(data.albums) { album ->
                                navigateToAlbum(album)
                            }


                            SongListView(data.singles, onMoreClicked = {
                                selectedSong = it
                                settingsClicked = true
                            }) {song ->
                                artistViewModel.getPlayerController().init(song, data.singles)

                            }
                        }
                    }
                }
            }
        }





        if (settingsClicked) {
            TrackBottomSheet(
                songSettingsSheetState = songSettingsSheetState,
                onAddToPlaylist = {
                    settingsClicked = false
                    addToPlaylistClicked = true
                },
                onNavigateToAlbum = {
                    navigateToAlbum(selectedSong.album)
                },
                onNavigateToArtist = {
                    navigateToArtist(selectedSong.getArtist())
                },
                onPlayNext = {},
                onShare = {},
            ) {
                settingsClicked = false
            }
        }



        if (addToPlaylistClicked) {
            AddToPlaylistBottomSheet(playlists = mutableListOf(),
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    navigateToNewPlaylist()
                }) {
                addToPlaylistClicked = false
            }
        }

    }


}

