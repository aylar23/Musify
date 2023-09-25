package com.musify.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow


@Composable
fun MiniPlayer(
    song: Song,
    onClick : () ->Unit
) {


    val songImagePainter = rememberAsyncImagePainter(
        model = song.image,
    )

    Row(
        modifier =  Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(color = Background)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(DarkGray),
            painter = songImagePainter,
            contentDescription = "artist image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = song.title,
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist.name,
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            containerColor = Yellow,
            contentColor = Black,
            onClick = {  },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.pause),
                contentDescription = "see more",
                tint = Black
            )

        }


    }
}