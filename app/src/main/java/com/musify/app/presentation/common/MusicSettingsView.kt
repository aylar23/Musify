@file:OptIn(ExperimentalMaterial3Api::class)

package com.musify.app.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun ActionsModelView(icon: Int, text: String, expandable: Boolean, playlist: Boolean, onClick: () -> Unit){
    Row(modifier = Modifier.padding(14.dp).clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(24.dp), painter = painterResource(id = icon), contentDescription = text + "button", tint = WhiteTextColor)
        if (!playlist){
            Text(modifier = Modifier
                .padding(horizontal = 10.dp)
                .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
        } else {
            Column {
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor)
                Text(modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f), text = text + " " + R.string.aydym, fontSize = 10.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Normal, color = WhiteTextColor)
            }
        }
        if (expandable){
            Icon(modifier = Modifier
                .size(14.dp), painter = painterResource(id = R.drawable.right_arrow), contentDescription = "See more icon", tint = WhiteTextColor)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bottomSheet(sheetState: SheetState, settingsClicked: MutableState<Boolean>) {
    ModalBottomSheet(
        containerColor = AlbumCoverBlackBG,
        sheetState = sheetState,
        onDismissRequest = { settingsClicked.value = false }) {
        ActionsModelView(expandable = true, icon = R.drawable.library_add, text = "Pleyliste Gos", playlist = false, onClick = {})
        ActionsModelView(expandable = false, icon = R.drawable.redo, text = "Indiki Edip Cal", playlist = false, onClick = {})
        ActionsModelView(expandable = true, icon = R.drawable.account_circle, text = "Bagshyny Gor", playlist = false, onClick = {})
        ActionsModelView(expandable = true, icon = R.drawable.album, text = "Albomy gor", playlist = false, onClick = {})
        ActionsModelView(expandable = false, icon = R.drawable.share, text = "Paylashmak", playlist = false, onClick = {})
    }
}