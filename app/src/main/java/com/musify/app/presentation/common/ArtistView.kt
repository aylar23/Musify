package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@Composable
fun ArtistView(artist: Artist) {
    //Pass Artist and change things
    Row(
        modifier = Modifier
            .background(color = DarkGray)
            .width(354.dp)
            .height(100.dp)
            .clip(
                shape = RoundedCornerShape(10.dp)
            )
    ){
        val artistImagePainter = rememberImagePainter(
            data = artist.image,
            builder = {
                placeholder(R.drawable.ic_launcher_background)
                crossfade(200)
            }
        )
        Image(
            modifier = Modifier
                .fillMaxWidth(.25f)
                .scale(1f)
                .padding(horizontal = 6.dp, vertical = 10.dp),
            painter = artistImagePainter,
            contentDescription = "artist image",
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center) {
            Text(
                text = artist.name,
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artist.songsCount.toString() + "songs",
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(modifier = Modifier.fillMaxHeight()){
            FloatingActionButton(
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.Center),
                backgroundColor = Yellow,
                onClick = { /*TODO*/ },
            ) {
                Icon(painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = "see more")
            }
        }


    }
}