package com.musify.app.presentation.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun KeyView(
    key: String,
    onSearch: (String) -> Unit,
) {

    Text(
        modifier = Modifier.clip(MaterialTheme.shapes.extraSmall).background(Surface).clickable { onSearch(key) }.padding(10.dp, 7.dp),
        text = key,
        style = TextStyle(
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontFamily = SFFontFamily,
            fontWeight = FontWeight.Normal,
            color = WhiteTextColor
        )
    )


}