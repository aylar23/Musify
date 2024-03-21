package com.musify.app.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.domain.models.User
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun SettingsTopAppBar (user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkGray, MaterialTheme.shapes.extraSmall)
            .clip(MaterialTheme.shapes.extraSmall)
            .padding(20.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                modifier = Modifier,
                text = user.name,
                lineHeight = 16.sp,
                fontSize = 16.sp,
                color = WhiteTextColor,
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(
                    modifier = Modifier,
                    text = stringResource(id = R.string.premium_user),
                    lineHeight = 12.sp,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "30 days left",
                    lineHeight = 12.sp,
                    fontSize = 12.sp,
                    color = GrayTextColor,
                    fontWeight = FontWeight.Normal
                )
            }

        }


    }

}