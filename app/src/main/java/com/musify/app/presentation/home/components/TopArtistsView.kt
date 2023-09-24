package com.musify.app.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.defaultArtist
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.presentation.common.ArtistView
import com.musify.app.presentation.common.PlaylistView
import com.musify.app.ui.theme.SFFontFamily


@Composable
fun TopArtistsView(artists: List<Artist>) {

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = stringResource(id = R.string.top),
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
        ) {
            for (artist in artists){
                ArtistView(artist)
            }
        }
    }
}