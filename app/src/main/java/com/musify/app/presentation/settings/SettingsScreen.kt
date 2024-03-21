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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.musify.app.BuildConfig
import com.musify.app.R
import com.musify.app.domain.models.User
import com.musify.app.presentation.settings.components.SettingsElements
import com.musify.app.presentation.settings.components.SettingsTopAppBar
import com.musify.app.presentation.settings.components.SubscriptionOptionsView
import com.musify.app.presentation.song.SongsViewModel
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SettingsScreen(
    navigator: DestinationsNavigator
) {

    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    val user = settingsViewModel.currentUser

    Scaffold(

    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            SettingsTopAppBar(user = user)

            Text(
                modifier = Modifier
                    .padding(top = 32.dp),
                text = stringResource(R.string.general_settings),
                color = WhiteTextColor,
                lineHeight = 6.25.em,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold
                ),
            )
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = stringResource(R.string.control_your_account_easily),
                color = GrayTextColor,
                lineHeight = 8.33.em,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = SFFontFamily,
                )
            )

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .background(DarkGray, MaterialTheme.shapes.extraSmall)
                    .clip(MaterialTheme.shapes.extraSmall)
            ) {

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
                    endText = BuildConfig.VERSION_NAME
                ) {

                }
                SettingsElements(
                    icon = R.drawable.comments,
                    text = stringResource(id = R.string.write_to_us),
                    expandable = true
                ) {

                }

            }

            SubscriptionOptionsView()

            Column {
                Row(modifier = Modifier
                    .clickable { }
                    .padding(20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.leave),
                        contentDescription = stringResource(id = R.string.log_out),
                        tint = WhiteTextColor
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 14.dp),
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

}