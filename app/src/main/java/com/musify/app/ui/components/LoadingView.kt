package com.musify.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingView(
    modifier: Modifier,
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator()

    }
}