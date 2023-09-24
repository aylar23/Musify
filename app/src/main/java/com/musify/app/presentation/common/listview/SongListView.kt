package com.musify.app.presentation.common.listview

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultSong
import com.musify.app.presentation.common.SongView
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun SongListView(
    songs: List<Song>,
    onMoreClicked: (Song) -> Unit,
    onClick: (Song) -> Unit,
) {

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = stringResource(id = R.string.songs),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight.Bold,
            )
        )


        Column(
            Modifier.padding(vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            songs.forEach { song ->
                SongView(
                    modifier = Modifier,
                    song = song,
                    onMoreClicked = {onMoreClicked(song)}
                ) {
                    onClick(song)
                }
            }

        }


    }
}