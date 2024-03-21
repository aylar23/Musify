package com.musify.app.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.musify.app.BuildConfig
import com.musify.app.R
import com.musify.app.ui.components.CustomButton
import com.musify.app.ui.theme.DarkGray
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.SurfaceSecond
import com.musify.app.ui.theme.WhiteTextColor


@Composable
fun SubscriptionOptionsView() {
    Column() {

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

            SubscriptionOptionView(){}
            SubscriptionOptionView(){}
            SubscriptionOptionView(){}

            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                text = R.string.enter_promo_code,
                onClick = {},
                containerColor = Inactive,
                contentColor = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.small
            )
        }

    }
}