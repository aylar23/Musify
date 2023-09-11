package com.musify.app.navigation

import androidx.annotation.DrawableRes
import com.musify.app.R

sealed class NavScreen(
    val route: String,
    @DrawableRes val icon: Int,
) {
    object Home : NavScreen(
        "home", R.drawable.ic_home
    )

    object Search : NavScreen(
        "search", R.drawable.ic_search
    )

    object MyPlaylists : NavScreen(
        "my_playlists", R.drawable.ic_grid
    )






}