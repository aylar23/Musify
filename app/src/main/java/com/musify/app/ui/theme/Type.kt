package com.musify.app.ui.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.musify.app.R


val SFFontFamily = FontFamily(
    Font(R.font.sf_light, FontWeight.Light),
    Font(R.font.sf_regular, FontWeight.Normal),
    Font(R.font.sf_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.sf_medium, FontWeight.Medium),
    Font(R.font.sf_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = SFFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = SFFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = SFFontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = SFFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = SFFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = SFFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = SFFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = SFFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = SFFontFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = SFFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = SFFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = SFFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = SFFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = SFFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = SFFontFamily)
)