package com.musify.app.navigation.screen

import androidx.annotation.DrawableRes
import com.musify.app.R

sealed class NavScreen(
    val route: String,
    @DrawableRes val icon: Int,
) {
    object Home : NavScreen(
        "nav_home", R.drawable.ic_home
    )
    object Search : NavScreen(
        "nav_search", R.drawable.ic_search
    )

    object MyPlaylists : NavScreen(
        "nav_my_playlists", R.drawable.ic_grid
    )






}