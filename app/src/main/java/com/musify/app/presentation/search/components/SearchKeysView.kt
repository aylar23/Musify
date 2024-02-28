package com.musify.app.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.GrayTextColor
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.Surface
import com.musify.app.ui.theme.WhiteTextColor
import com.musify.app.ui.utils.clickWithoutIndication


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchKeysView(
    keys: List<String>,
    onDelete: () -> Unit,
    onSearch: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        Row(
            modifier = Modifier.padding(horizontal = 20.dp),

            ) {

            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.recent_searches),
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = GrayTextColor,
                )
            )

            Text(
                modifier = Modifier.padding(5.dp).clickWithoutIndication { onDelete() },
                text = stringResource(id = R.string.clear), style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 10.sp,
                    fontFamily = SFFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            )

        }


        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(keys) { key ->
                KeyView(
                    key,
                    onSearch
                )
            }

        }
    }
}