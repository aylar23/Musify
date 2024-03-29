package com.musify.app.presentation.playlist.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.musify.app.R
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.SFFontFamily
import com.musify.app.ui.theme.WhiteTextColor



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingTopAppBar(
    title:String,
    scrollBehaviour: TopAppBarScrollBehavior,
    navigateUp: () ->Unit
) {

    LargeTopAppBar(
        title = {
            Text(
                text = title,
                color = WhiteTextColor.copy(alpha = scrollBehaviour.state.overlappedFraction),
                fontSize = 16.sp,
                fontFamily = SFFontFamily,
                fontWeight = FontWeight(700)
            )
        },
        scrollBehavior = scrollBehaviour,
        navigationIcon = {

            IconButton(onClick = navigateUp ) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow),
                    contentDescription = "profile button",
                    tint = WhiteTextColor,
                    modifier = Modifier
                        .size(24.dp)

                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Background.copy(alpha = scrollBehaviour.state.overlappedFraction)),
    )

}