package com.musify.app.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@Composable
fun SongView(
    @SuppressLint("ModifierParameter") modifier: Modifier? = null,
    song: Song,
    onMoreClicked: () -> Unit,
    onClick: () -> Unit,
) {

    val songImagePainter = rememberAsyncImagePainter(
        model = song.image,
        placeholder = painterResource(id = R.drawable.mock_cover)

    )

    Row(
        modifier = modifier?.clickable { onClick() }?.padding(5.dp) ?: Modifier
            .clickable { onClick() }
            .padding(start = 20.dp, top = 5.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(50.dp).background(Yellow),
            painter = songImagePainter,
            contentDescription = "song Image"

        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = song.title,
                fontFamily = SFFontFamily,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist.name,
                fontFamily = SFFontFamily,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                color = GrayTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = { onMoreClicked() },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.song_setting),
                contentDescription = "song setting",
                tint = WhiteTextColor
            )
        }
    }
}