package com.musify.app.ui.components.listview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.presentation.home.HomeViewModel
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun SongGridListView(
    homeViewModel: HomeViewModel,
    songs: List<Song>,
    onMoreClicked: (Song) -> Unit,
    onClick: (Song) -> Unit,
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    if (songs.isNotEmpty()) {
        Column {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.hit_songs),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            )

            LazyHorizontalGrid(
                modifier = Modifier.height(300.dp),
                rows = GridCells.Fixed(4),
                contentPadding = PaddingValues(20.dp, 15.dp)
            ) {
                items(songs) { song ->
                    SongView(
                        modifier = Modifier.width(screenWidth * 0.90f),
                        playerController = homeViewModel.getPlayerController(),
                        song = song,
                        downloadTracker = homeViewModel.getDownloadTracker(),
                        onMoreClicked = { onMoreClicked(song) }
                    ) {
                        onClick(song)
                    }
                }
            }
        }
    }
}