package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.defaultPlaylist
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun MainPlaylistView(playlist: Playlist) {

    val playlistImagePainter = rememberAsyncImagePainter(
        model = playlist.image,
    )

    Column(modifier = Modifier
        .clip(shape = MaterialTheme.shapes.medium)
        .clickable { }
        .background(AlbumCoverBlackBG)) {

        Image(
            modifier = Modifier
                .height(150.dp)
                .width(160.dp)
                .background(DarkGray),
            painter = playlistImagePainter,
            contentDescription = "Album Cover",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = playlist.name,
                fontFamily = SFFontFamily,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = playlist.songsCount.toString(),
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }

}


@Preview
@Composable
fun PlaylistViewPre() {
    MainPlaylistView(defaultPlaylist)
}