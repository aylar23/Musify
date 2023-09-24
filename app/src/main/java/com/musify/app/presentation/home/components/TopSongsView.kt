package com.musify.app.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.domain.models.defaultAlbum
import com.musify.app.domain.models.defaultSong
import com.musify.app.presentation.common.AlbumView
import com.musify.app.presentation.common.SongView
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun TopSongsView(songs: List<Song>) {

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.new_albums),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight.Bold,
            )
        )


        LazyHorizontalGrid(
            modifier = Modifier.height(250.dp),
            rows = GridCells.Fixed(4),
            contentPadding = PaddingValues(20.dp, 15.dp),

        ){
            items(10) {
                SongView(modifier = Modifier.width(screenWidth*0.90f), song = defaultSong){}
            }
        }


    }
}