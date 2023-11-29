package com.musify.app.presentation.artist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
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
import com.musify.app.ui.components.toolbar.CollapsingToolbarScaffold
import com.musify.app.ui.components.toolbar.ScrollStrategy
import com.musify.app.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Inactive
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
    navigateToAlbum: (Long) -> Unit,
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
    val state = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
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
                    )
            )

            Row(
                modifier = Modifier
                    .padding(padding, 16.dp, 16.dp, 16.dp)
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)

            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                    text = uiState.data?.name ?: "",
                    style = TextStyle(color = Color.White, fontSize = textSize),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                    )

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


        }) {




        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),

        ) {


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
                                navigateToAlbum(album.playlistId)
                            }


                            SongListView(
                                data.singles, onMoreClicked = {
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
                    selectedSong.album?.playlistId?.let { navigateToAlbum(it) }
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

