@file:OptIn(ExperimentalMaterial3Api::class)

package com.musify.app.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
fun bottomSheet(settingsClicked: MutableState<Boolean>, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()


    if (settingsClicked.value){
        ModalBottomSheet(
            containerColor = AlbumCoverBlackBG,
            sheetState = sheetState,
            onDismissRequest = { onDismiss }) {
            ActionsModelView(expandable = true, icon = R.drawable.library_add, text = "Pleyliste Gos")
            ActionsModelView(expandable = false, icon = R.drawable.redo, text = "Indiki Edip Cal")
            ActionsModelView(expandable = true, icon = R.drawable.account_circle, text = "Bagshyny Gor")
            ActionsModelView(expandable = true, icon = R.drawable.album, text = "Albomy gor")
            ActionsModelView(expandable = false, icon = R.drawable.share, text = "Paylashmak")
        }
    }
}



@Composable
fun ActionsModelView(icon: Int, text: String, expandable: Boolean){
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