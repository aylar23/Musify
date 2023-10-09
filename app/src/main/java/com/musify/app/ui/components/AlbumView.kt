package com.musify.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor



@Composable
fun AlbumView(
    album: Playlist,
    onClick: ()->Unit
) {

    val albumImagePainter = rememberAsyncImagePainter(
        model = album.getPlaylistImage(),
    )

    Box(modifier = Modifier
        .height(210.dp)
        .aspectRatio(1.5f)
        .clip(shape = MaterialTheme.shapes.large)
        .background(DarkGray)
        .clickable { onClick() }
    ){

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = albumImagePainter,
            contentDescription = "Album Image",
            contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)) {
            Text(
                text = album.name,
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = album.getArtistsName(),
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

