package com.musify.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Artist
import com.musify.app.domain.models.defaultArtist
import com.musify.app.ui.theme.Black
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@Composable
fun ArtistView(
     artist: Artist,
     onClick :()->Unit
) {

    val artistImagePainter = rememberAsyncImagePainter(
        model = artist.image,
    )

    Row(
        modifier =  Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(shape = MaterialTheme.shapes.large)
            .clickable { onClick() }
            .background(color = DarkGray)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){

        Image(
            modifier = Modifier
                .size(80.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(DarkGray),
            painter = artistImagePainter,
            contentDescription = "artist image",
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
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

        FloatingActionButton(
            modifier = Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            containerColor = Yellow,
            contentColor = Black,
            onClick = {  },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = "see more",
                tint = Black)

        }


    }
}

