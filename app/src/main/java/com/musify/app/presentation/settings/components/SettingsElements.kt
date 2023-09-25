package com.musify.app.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.theme.Yellow

@Composable
fun SettingsElements(icon: Int, text: String, expandable: Boolean, endText: String? = null, onClick: () -> Unit){

    Row(modifier = Modifier
        .clickable { onClick() }
        .padding(14.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(modifier = Modifier
            .size(24.dp), painter = painterResource(id = icon), contentDescription = text + "button", tint = WhiteTextColor
        )

        Text(modifier = Modifier
            .padding(horizontal = 14.dp)
            .weight(1f), text = text, fontSize = 16.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Bold, color = WhiteTextColor
        )

        if (expandable){
            Icon(modifier = Modifier
                .size(14.dp), painter = painterResource(id = R.drawable.right_arrow), contentDescription = "See more icon", tint = WhiteTextColor
            )
        } else {
            if (endText != null){
                Text(modifier = Modifier, text = text, fontSize = 15.sp, fontFamily = SFFontFamily, fontWeight = FontWeight.Normal, color = Yellow)
            }
        }
    }

}