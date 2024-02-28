package com.musify.app.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun HomeTopAppBar(
    onSettingsClicked: () -> Unit,
    onSearchClicked: () -> Unit,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 10.dp, top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)

    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.welcome),
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 22.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            )

            Text(
                text = stringResource(id = R.string.all_songs_in_one_place),
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            )
        }

        IconButton(
            onClick = onSearchClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_explore),
                contentDescription = "playlist setting",
                tint = WhiteTextColor
            )
        }

        IconButton(
            onClick = onSettingsClicked,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "playlist setting",
                tint = WhiteTextColor
            )
        }
    }

}