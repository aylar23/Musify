package com.musify.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun ActionsModelView(
    icon: Int,
    mainText: String,
    expandable: Boolean,
    trailingIcon: Int = R.drawable.right_arrow,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onClick() }
        .padding(20.dp, 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
//                modifier = Modifier.size(24.dp),
            painter = painterResource(id = icon),
            contentDescription = mainText + "button",
            tint = WhiteTextColor
        )
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 15.dp
                )
                .weight(1f),
            text = mainText,
            fontSize = 16.sp,
            fontFamily = SFFontFamily,
            fontWeight = FontWeight.Bold,
            color = WhiteTextColor
        )

        if (expandable) {
            Icon(
//                modifier = Modifier.size(24.dp),
                painter = painterResource(id = trailingIcon),
                contentDescription = "See more icon",
                tint = WhiteTextColor
            )
        }
    }
}


@Composable
fun LocalPlaylistSelectionView(
    playlist: Playlist,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .clickable { onClick() }
        .background(SurfaceSecond)
        .padding(15.dp, 13.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = R.drawable.playlist_add_check),
            contentDescription = "button",
            tint = WhiteTextColor
        )


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = playlist.name ,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight.Bold,
                color = WhiteTextColor
            )
            Text(
                text = stringResource(id = R.string.song_count, playlist.songsCount.toString()),
                fontSize = 10.sp,
                lineHeight = 12.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight.Normal,
                color = WhiteTextColor
            )
        }


    }
}

@Preview
@Composable
fun fjnfjfn () {
    LocalPlaylistSelectionView(Playlist(0L, "name")){

    }
}

