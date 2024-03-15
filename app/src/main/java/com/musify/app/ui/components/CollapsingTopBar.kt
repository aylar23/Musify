package com.musify.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingSmallTopAppBar(
    title: String,
    trailingIcon: Int? = null,
    navigationIcon: Int = R.drawable.left_arrow,
    onIconClick: () -> Unit = {},
    goBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { goBack() }) {
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

        if (trailingIcon != null) {
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


}