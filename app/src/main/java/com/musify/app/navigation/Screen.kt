package com.musify.app.navigation

import com.musify.app.R


sealed class Screen(
    val route: String
) {

    object Home : Screen(
        "home"
    )


    object Search : Screen(
        "search"
    )


    object Login : Screen(
        "login"
    )

    object Artist : Screen(
        "artist"
    )


    object Playlist : Screen(
        "playlist"
    )


    object Settings : Screen(
        "playlist"
    )

}