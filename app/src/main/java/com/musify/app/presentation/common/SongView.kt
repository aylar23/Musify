package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun SongView(song: Song) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically) {
        Row(modifier = Modifier.fillMaxWidth(.8f)) {
            val songImagePainter = rememberImagePainter(
                data = song.image,
                builder = {
                    placeholder(R.drawable.mock_cover)
                    crossfade(200)
                }
            )
            Image(
                modifier = Modifier.size(50.dp),
                painter = songImagePainter,
                contentDescription = "song Image")
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.title,
                    fontFamily = SFFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artist.name,
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
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(16.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.song_setting),
                contentDescription = "song setting",
                tint = WhiteTextColor
                )
        }
    }
}