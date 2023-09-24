package com.musify.app.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import com.musify.app.domain.models.User
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor

@Composable
fun SettingsScreen(user: User){
    val premium = true

    Column (modifier = Modifier
        .fillMaxSize()
        .background(AlbumCoverBlackBG), verticalArrangement = Arrangement.SpaceBetween){
        Column (modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp, 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (premium) {
                    Column(
                        verticalArrangement = Arrangement.Center) {
                        Text(
                            modifier = Modifier,
                            text = user.name + " " + user.lastName,
                            lineHeight = 26.sp,
                            fontSize = 22.sp,
                            color = WhiteTextColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier,
                            text = stringResource(id = R.string.premium_user),
                            lineHeight = 18.sp,
                            fontSize = 15.sp,
                            color = GrayTextColor,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.verified),
                            contentDescription = "verified",
                            tint = WhiteTextColor
                        )
                    }

                }else{
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.my_playlists),
                        lineHeight = 22.sp,
                        fontSize = 22.sp,
                        color = WhiteTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGray)
                    .padding(horizontal = 14.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            SettingsElements(
                icon = R.drawable.account_circle,
                text = stringResource(id = R.string.my_account),
                expandable = true
            ) {

            }
            SettingsElements(
                icon = R.drawable.language,
                text = stringResource(id = R.string.language),
                expandable = false,
                endText = stringResource(R.string.english)
            ) {

            }
            SettingsElements(
                icon = R.drawable.version,
                text = stringResource(id = R.string.version),
                expandable = false,
                endText = "1.0"
            ) {

            }
            SettingsElements(
                icon = R.drawable.comments,
                text = stringResource(id = R.string.write_to_us),
                expandable = true
            ) {

            }

        }
        Column {
            Row(modifier = Modifier
                .clickable { }
                .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    painter = painterResource(id = R.drawable.leave),
                    contentDescription = stringResource(id = R.string.log_out),
                    tint = WhiteTextColor
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 14.dp),
                    text = stringResource(id = R.string.log_out),
                    fontSize = 16.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = WhiteTextColor
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}