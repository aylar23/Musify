package com.musify.app.navigation

import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Inactive
import com.musify.app.ui.theme.NoRippleTheme
import com.musify.app.ui.theme.Yellow


@Composable
fun BottomNavigationBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavScreen.Home,
        NavScreen.Search,
        NavScreen.MyPlaylists,
    )
    CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = Color.Black,

            ) {
            items.forEach { item ->

                val selected = currentRoute == item.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.route,
                            tint = if(selected) Yellow else Color.White

                        )
                    },
                    label = {
                            Text(text = stringResource(id = item.label),
                                color = if(selected) Yellow else Color.White,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,

                            )
                    },
                    alwaysShowLabel = true,
                    selected = selected,
                    onClick = {

                        navController.navigate(item.route) {

                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults
                        .colors(
                            selectedIconColor = Yellow,
                            selectedTextColor = Yellow,
                            indicatorColor = Background
                        )
                )
            }
        }
    }

}