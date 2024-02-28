package com.musify.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingSmallTopAppBar(
    title:String,
    trailingIcon: Int,
    navigationIcon: Int = R.drawable.left_arrow,
    onIconClick: () -> Unit,
    goBack: () -> Unit,
) {
    Row( modifier = Modifier
        .fillMaxWidth().background(AlbumCoverBlackBG),
        verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {  goBack() }) {
            Icon(
                tint = WhiteTextColor,
                painter = painterResource(id = navigationIcon),
                contentDescription = stringResource(id = R.string.go_back)
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = WhiteTextColor,
            fontFamily = SFFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            modifier = Modifier.padding(5.dp, 20.dp),
            onClick = { onIconClick() },
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = trailingIcon),
                tint = WhiteTextColor,
                contentDescription = ""
            )
        }



    }



}