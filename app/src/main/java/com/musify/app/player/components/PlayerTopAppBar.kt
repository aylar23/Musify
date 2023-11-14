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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopAppBar(
    playlist: Playlist?,
    goBack: () -> Unit,
) {
    TopAppBar(title = {

        playlist?.let {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = playlist.name ,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(700),
                    )
                )

                Text(
                    text = if(playlist.year == 0 ) "" else playlist.year.toString(),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        fontFamily = SFFontFamily,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF828282),
                    )
                )
            }
        }


    },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        navigationIcon = {
            IconButton(onClick = {  goBack() }) {
                Icon(
                    tint = WhiteTextColor,
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AlbumCoverBlackBG),
        actions = {

//            IconButton(onClick = {  }) {
                Icon(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    painter = painterResource(id = R.drawable.ic_cast),
                    contentDescription = null
                )
//            }
        }

    )


}