package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun PlaylistView(playlist: Playlist) {

    Column(modifier = Modifier
        .height(202.dp)
        .width(160.dp)
        .clip(shape = RoundedCornerShape(7.dp))
        .background(AlbumCoverBlackBG)) {
        val playlistImagePainter = rememberImagePainter(
            data = playlist.image,
            builder = {
                placeholder(R.drawable.mock_cover)
                crossfade(200)
            }
        )
        Image(
            modifier = Modifier
                .height(150.dp)
                .width(160.dp),
            painter = playlistImagePainter,
            contentDescription = "Album Cover",
            contentScale = ContentScale.FillBounds
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = playlist.name,
                fontFamily = SFFontFamily,
                fontSize = 14.sp,
                lineHeight = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = (playlist.songsCount ?: 0).toString(),
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }

}