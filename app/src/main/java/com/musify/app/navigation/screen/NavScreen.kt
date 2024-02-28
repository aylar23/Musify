package com.musify.app.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.musify.app.R

sealed class NavScreen(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val activeIcon: Int,
) {
    object Home : NavScreen(
        "nav_home", R.string.main,R.drawable.ic_home, R.drawable.ic_home_active
    )
    object Search : NavScreen(
        "nav_search", R.string.search, R.drawable.ic_explore, R.drawable.ic_explore_active
    )

    object MyPlaylists : NavScreen(
        "nav_my_playlists",R.string.library, R.drawable.ic_library, R.drawable.ic_library_active
    )






}