@file:OptIn(ExperimentalMaterial3Api::class)

package com.musify.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Playlist
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun ActionsModelView(icon: Int, text: String, expandable: Boolean, playlist: Playlist?, onClick: () -> Unit){
    Row(modifier = Modifier
        .clickable { onClick() }
        .padding(14.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(24.dp), painter = painterResource(id = icon), contentDescription = text + "button", tint = WhiteTextColor)
        if (playlist == null){
            Text(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
        } else {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp), text = playlist.image, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp), text = playlist.songsCount.toString() + " " + R.string.songs, fontSize = 10.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Normal, color = WhiteTextColor)
            }
        }
        if (expandable){
            Icon(modifier = Modifier
                .size(14.dp), painter = painterResource(id = R.drawable.right_arrow), contentDescription = "See more icon", tint = WhiteTextColor)
        }
    }
}


