package com.musify.app.navigation.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.musify.app.R
import com.musify.app.presentation.destinations.HomeScreenDestination
import com.musify.app.presentation.destinations.MyPlaylistsScreenDestination
import com.musify.app.presentation.destinations.SearchScreenDestination
import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

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

enum class BottomBarDestination(
    val direction: DirectionDestinationSpec,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val activeIcon: Int,

    ) {
    Home(HomeScreenDestination, R.string.main,R.drawable.ic_home, R.drawable.ic_home_active),
    Search(SearchScreenDestination, R.string.search,  R.drawable.ic_explore, R.drawable.ic_explore_active),
    Library(MyPlaylistsScreenDestination, R.string.library, R.drawable.ic_library, R.drawable.ic_library_active),
}

@NavGraph
annotation class LoginNavGraph(
    val start: Boolean = false
)