package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Album
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun AlbumView(album: Album) {
    Box(modifier = Modifier
        .width(347.dp)
        .height(218.dp)
        .clip(shape = RoundedCornerShape(10.dp))){

        val albumImagePainter = rememberImagePainter(
            data = album.image,
            builder = {
                placeholder(R.drawable.ic_launcher_background)
                crossfade(200)
            }
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = albumImagePainter,
            contentDescription = "Album Image",
            contentScale = ContentScale.Crop)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 15.dp, vertical = 17.dp)) {
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
                text = album.artist.name,
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