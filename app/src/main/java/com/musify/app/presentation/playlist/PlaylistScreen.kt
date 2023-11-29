package com.musify.app.presentation.playlist

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultArtist
import com.musify.app.presentation.playlist.components.CollapsingTopAppBar
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.components.LoadingView
import com.musify.app.ui.components.NetworkErrorView
import com.musify.app.ui.components.SongView
import com.musify.app.ui.components.SwipeableSongView
import com.musify.app.ui.components.bottomsheet.AddToPlaylistBottomSheet
import com.musify.app.ui.components.bottomsheet.TrackBottomSheet
import com.musify.app.ui.components.toolbar.CollapsingToolbarScaffold
import com.musify.app.ui.components.toolbar.ScrollStrategy
import com.musify.app.ui.components.toolbar.rememberCollapsingToolbarScaffoldState
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.TransparentColor
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow
import java.lang.reflect.Type


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
        playlistViewModel.getPlaylist(id, type)
    }

    lateinit var selectedSong: Song

    var settingsClicked by remember {
        mutableStateOf(false)
    }

    var addToPlaylistClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val uiState by playlistViewModel.uiState.collectAsState()

    val playlistSheetState = rememberModalBottomSheetState()

    val songSettingsSheetState = rememberModalBottomSheetState()


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
                    .road(whenCollapsed = Alignment.TopStart, whenExpanded = Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)

            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                    text = uiState.data?.name ?: "",
                    style = TextStyle(color = White, fontSize = textSize),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                    )

                OutlinedIconButton(
                    modifier = Modifier.size(45.dp),
                    onClick = { uiState.data?.let { playlistViewModel.savePlaylist(it) } },
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.dp, color = Inactive
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        tint = WhiteTextColor,
                        contentDescription = ""
                    )
                }

                OutlinedIconButton(
                    modifier = Modifier.size(45.dp),
                    onClick = { uiState.data?.let { playlistViewModel.savePlaylist(it) } },
                    shape = CircleShape,
                    border = BorderStroke(
                        width = 1.dp, color = Inactive
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.library_add),
                        tint = WhiteTextColor,
                        contentDescription = ""
                    )
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


        }) {


        LazyColumn(modifier = Modifier) {
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
                        onClick = { },
                        containerColor = Yellow,
                        contentColor = Black,
                        leadingIcon = R.drawable.play

                    )

                    Spacer(modifier = Modifier.weight(.1f))

                    CustomButton(
                        modifier = Modifier.weight(1f),
                        text = R.string.shuffle,
                        onClick = { },
                        containerColor = DarkGray,
                        contentColor = WhiteTextColor,
                        leadingIcon = R.drawable.shuffle
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
                        items(data,
                            key = {
                                it.songId
                            }
                        ) { song ->

                            SwipeableSongView(song = song,
                                onMoreClicked = {
                                    selectedSong = it
                                    settingsClicked = true

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
            AddToPlaylistBottomSheet(
                playlists = mutableListOf(),
                playlistSheetState = playlistSheetState,
                onCreateNewPlaylist = {
                    navigateToNewPlaylist()
                }
            ) {
                addToPlaylistClicked = false
            }
        }

    }
}

//    Scaffold(
//        modifier = Modifier.padding(paddingValues = paddingValues),
//        topBar = {
//            CollapsingTopAppBar(
//                title = uiState.data?.name ?: "",
//                scrollBehaviour = scrollBehavior
//            ) {
//                navigateUp()
//            }
//        }
//    ) { padding ->


//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(Background)
//            .graphicsLayer {
//                translationY = scrollBehavior.state.contentOffset
//            }) {
//            Image(
//                painter = rememberAsyncImagePainter(model = uiState.data?.image ?: ""),
//                contentDescription = "",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f),
//                contentScale = ContentScale.Crop
//            )
//        }


//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .nestedScroll(scrollBehavior.nestedScrollConnection)
//        ) {
//            item {
//                Column(
//                    modifier = Modifier.background(
//                        brush = Brush.verticalGradient(
//                            startY = 0f, endY = 350f, colors = listOf(
//                                TransparentColor, AlbumCoverBlackBG
//                            )
//                        )
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 160.dp)
//                            .padding(vertical = 4.dp, horizontal = 14.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = uiState.data?.name ?: "",
//                            color = WhiteTextColor,
//                            fontFamily = SFFontFamily,
//                            fontWeight = FontWeight.Bold
//                        )
//                        OutlinedIconButton(
//                            onClick = { uiState.data?.let { playlistViewModel.savePlaylist(it) } },
//                            shape = RoundedCornerShape(50.dp),
//                            border = BorderStroke(
//                                width = 1.dp, color = Inactive
//                            )
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.group),
//                                tint = WhiteTextColor,
//                                contentDescription = ""
//                            )
//                        }
//                    }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 16.dp, horizontal = 10.dp),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//
//                        CustomButton(
//                            modifier = Modifier.weight(1f),
//                            text = R.string.play_all,
//                            onClick = { },
//                            containerColor = Yellow,
//                            contentColor = Black,
//                            leadingIcon = R.drawable.play
//
//                        )
//
//                        Spacer(modifier = Modifier.weight(.1f))
//
//                        CustomButton(
//                            modifier = Modifier.weight(1f),
//                            text = R.string.shuffle,
//                            onClick = { },
//                            containerColor = DarkGray,
//                            contentColor = WhiteTextColor,
//                            leadingIcon = R.drawable.shuffle
//                        )
//
//
//                    }
//                }
//
//            }


//            when {
//                uiState.isLoading -> {
//                    item {
//
//                        LoadingView(
//                            Modifier
//                                .fillMaxWidth()
//                                .fillMaxHeight()
//                                .background(Background)
//                        )
//                    }
//                }
//
//                uiState.isFailure -> {
//                    item {
//
//                        NetworkErrorView(
//                            Modifier
//                                .fillMaxWidth()
//                                .fillMaxHeight()
//                                .background(Background)
//                        ) {
//                            playlistViewModel.getPlaylist(id, type)
//                        }
//                    }
//                }
//
//                uiState.isSuccess -> {
//                    uiState.data?.songs?.let { data ->
//                        items(data,
//                            key = {
//                               it.songId
//                            }
//                        ) { song ->
//                            SongView(song = song,
//                                onMoreClicked = {
//                                    selectedSong = it
//                                    settingsClicked = true
//
//                                }
//                            ) {
//                                playlistViewModel.getPlayerController().init(song, data)
//
//                            }
//                        }
//                    }
//
//                }
//
//            }
//        }
//        if (settingsClicked) {
//            TrackBottomSheet(
//                songSettingsSheetState = songSettingsSheetState,
//                onAddToPlaylist = {
//                    settingsClicked = false
//                    addToPlaylistClicked = true
//                },
//                onNavigateToAlbum = {
//                    selectedSong.album?.playlistId?.let { navigateToAlbum(it) }
//                },
//                onNavigateToArtist = {
//                    navigateToArtist(selectedSong.getArtist())
//                },
//                onPlayNext = {},
//                onShare = {},
//            ) {
//                settingsClicked = false
//            }
//        }
//
//
//
//        if (addToPlaylistClicked) {
//            AddToPlaylistBottomSheet(
//                playlists = mutableListOf(),
//                playlistSheetState = playlistSheetState,
//                onCreateNewPlaylist = {
//                    navigateToNewPlaylist()
//                }
//            ) {
//                addToPlaylistClicked = false
//            }
//        }
//    }








