package com.musify.app.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopAppBar(
    selectedSong: Song?,
    onMoreClicked: () -> Unit,
    goBack: () -> Unit,
) {
    TopAppBar(title = {

        selectedSong?.let {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = selectedSong.albumName ?: "",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(700),
                    ),maxLines = 1,
                    overflow = TextOverflow.Ellipsis,

                )

                Text(
                    text = if (selectedSong.albumYear == 0L || selectedSong.albumYear == null) "" else selectedSong.albumYear.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF828282),
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }
        }


    },
        modifier = Modifier
            .fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = { goBack() }) {
                Icon(
                    tint = WhiteTextColor,
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),

        actions = {
            IconButton(onClick = { onMoreClicked() }) {
                Icon(
                    tint = WhiteTextColor,
                    painter = painterResource(id = R.drawable.ic_more_hor),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }
        }
    )


}