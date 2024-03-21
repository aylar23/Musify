package com.musify.app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.musify.app.navigation.screen.BottomBarDestination
import com.musify.app.navigation.screen.NavScreen
import com.musify.app.presentation.NavGraphs
import com.musify.app.presentation.appCurrentDestinationAsState
import com.musify.app.presentation.destinations.Destination
import com.musify.app.presentation.startAppDestination
import com.musify.app.ui.theme.AlbumCoverBlackBG
import com.musify.app.ui.theme.Background
import com.musify.app.ui.theme.Yellow
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.utils.isRouteOnBackStack


@Composable
fun BottomNavigationBar(
    navController: NavController,
    onSelect: (String) -> Unit
) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentDestination: Destination =
        navController.appCurrentDestinationAsState().value ?: NavGraphs.root.startAppDestination


    NavigationBar(
        containerColor = AlbumCoverBlackBG,
        contentColor = Color.Black,

        ) {
        BottomBarDestination.values().forEach { destination ->
            val selected = currentDestination == destination.direction
            val isCurrentDestOnBackStack = navController.isRouteOnBackStack(destination.direction)
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = if (selected) destination.activeIcon else destination.icon),
                        contentDescription = null,
                        tint = if (selected) Yellow else Color.White

                    )
                },
                label = {
                    Text(
                        text = stringResource(id = destination.label),
                        color = if (selected) Yellow else Color.White,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,

                        )
                },
                alwaysShowLabel = true,
                selected = currentDestination == destination.direction,
                onClick = {
                    if (isCurrentDestOnBackStack) {
                        navController.popBackStack(destination.direction, false)
                        return@NavigationBarItem
                    }
                    navController.navigate(destination.direction) {
                        launchSingleTop = true
                        restoreState = true
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                    }

                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Yellow,
                    selectedTextColor = Yellow,
                    indicatorColor = Background
                )
            )
        }
    }

}