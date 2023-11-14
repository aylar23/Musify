package com.musify.app.ui.components.listview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.components.SongView
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun SongListView(
    songs: List<Song>,
    onMoreClicked: (Song) -> Unit,
    onClick: (Song) -> Unit,
) {

    Column(modifier = Modifier.background(Background).padding(horizontal = 20.dp)) {
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
                    song = song,
                    onMoreClicked = {onMoreClicked(song)}
                ) {
                    onClick(song)
                }
            }

        }


    }
}