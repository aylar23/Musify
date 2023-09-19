package com.musify.app.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.Song
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun MusicSettingsView(song: Song?, settingsClicked: MutableState<Boolean>, hide: () -> Unit) {

    AnimatedVisibility(
        visible = settingsClicked.value,
        enter = slideInVertically(
            initialOffsetY = { it/2 },
            animationSpec = tween(100)
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                it / 2
            },
            animationSpec = tween(100)
        )
    ) {
        Box(modifier = Modifier
            .fillMaxSize()){
            Box(modifier = Modifier
                .fillMaxSize()
                .alpha(.3f)
                .background(AlbumCoverBlackBG)
                .clickable { hide() }
            )
            Column(modifier = Modifier
                .clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(AlbumCoverBlackBG)
                .align(Alignment.BottomCenter)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.weight(1.2f))
                    HorizontalDivider(
                        modifier = Modifier.weight(.2f),
                        thickness = 1.dp,
                        color = WhiteTextColor)
                    Spacer(modifier = Modifier.weight(1.2f))
                }
                ActionsModelView(expandable = true, icon = R.drawable.library_add, text = "Pleyliste Gos")
                ActionsModelView(expandable = false, icon = R.drawable.redo, text = "Indiki Edip Cal")
                ActionsModelView(expandable = true, icon = R.drawable.account_circle, text = "Bagshyny Gor")
                ActionsModelView(expandable = true, icon = R.drawable.album, text = "Albomy gor")
                ActionsModelView(expandable = false, icon = R.drawable.share, text = "Paylashmak")
            }

        }
    }

}


@Composable
private fun ActionsModelView(icon: Int, text: String, expandable: Boolean){
    Row(modifier = Modifier.padding(14.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(24.dp), painter = painterResource(id = icon), contentDescription = text + "button", tint = WhiteTextColor)
        Text(modifier = Modifier
            .padding(horizontal = 10.dp)
            .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
        if (expandable){
            Icon(modifier = Modifier
                .size(14.dp), painter = painterResource(id = R.drawable.right_arrow), contentDescription = "See more icon", tint = WhiteTextColor)
        }
    }
}