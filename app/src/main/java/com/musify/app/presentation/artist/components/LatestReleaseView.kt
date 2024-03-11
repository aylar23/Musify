package com.musify.app.presentation.artist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.LatestRelease
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun LatestReleaseView(
    latestRelease: LatestRelease,
    navigateToAlbum :(Playlist)->Unit,
    playSong: (Song) ->Unit
    ) {

    val imagePainter = rememberAsyncImagePainter(
        model = latestRelease.getImage(),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .clip(shape = MaterialTheme.shapes.large)
            .clickable {
                if (latestRelease.isAlbum()) navigateToAlbum(latestRelease.album!!)
                else if (latestRelease.isSong()) playSong(latestRelease.song!!)
            }
            .background(color = DarkGray)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){



        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = stringResource(id = R.string.latest_release),
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = latestRelease.getName(),
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = latestRelease.getYear(),
                fontFamily = SFFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }


        Image(
            modifier = Modifier
                .size(70.dp)
                .clip(shape = MaterialTheme.shapes.small)
                .background(DarkGray),
            painter = imagePainter,
            contentDescription = "artist image",
            contentScale = ContentScale.Crop
        )

    }
}