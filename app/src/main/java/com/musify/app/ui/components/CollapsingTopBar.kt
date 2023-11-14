package com.musify.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingSmallTopAppBar(
    trailingIcon: Int,
    navigationIcon: Int = R.drawable.left_arrow,
    trailingIconDescription: String,
    goBack: () -> Unit,
) {
    TopAppBar(title = {
        Text(
            text = "",
            color = WhiteTextColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        navigationIcon = {
            IconButton(onClick = {  goBack() }) {
                Icon(
                    tint = WhiteTextColor,
                    painter = painterResource(id = navigationIcon),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AlbumCoverBlackBG),
        actions = {
            Icon(
                modifier = Modifier.padding(horizontal = 20.dp),
                painter = painterResource(id = trailingIcon),
                contentDescription = trailingIconDescription
            )
        }

    )


}