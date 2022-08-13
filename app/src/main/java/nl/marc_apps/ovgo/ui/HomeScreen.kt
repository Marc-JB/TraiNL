package nl.marc_apps.ovgo.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DepartureBoard
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.ui.theme.AppTheme

sealed class HomeScreenDestination(
    val destination: Destination,
    @StringRes val titleRes: Int,
   val icon: ImageVector
) {
    object DepartureBoard : HomeScreenDestination(
        DepartureBoardDestination(),
        R.string.departure_board,
        Icons.Rounded.DepartureBoard
    )

    object Disruptions : HomeScreenDestination(
        DisruptionsDestination,
        R.string.disruptions,
        Icons.Rounded.Error
    )

    object Maintenance : HomeScreenDestination(
        MaintenanceDestination,
        R.string.maintenance,
        Icons.Rounded.Warning
    )
}

@Composable
fun HomeScreen() {
    AppTheme {
        val navController = rememberNavController()

        val items = listOf(
            HomeScreenDestination.DepartureBoard,
            HomeScreenDestination.Disruptions,
            HomeScreenDestination.Maintenance
        )

        BoxWithConstraints {
            val showNavigationRail by remember(maxWidth, maxHeight) {
                derivedStateOf {
                    maxWidth > maxHeight && maxWidth >= 600.dp
                }
            }

            if (showNavigationRail) {
                HomeScreenWithNavigationRail(navController, items)
            } else {
                HomeScreenWithBottomNavigation(navController, items)
            }
        }
    }
}

@Composable
fun HomeScreenWithBottomNavigation(
    navController: NavHostController,
    items: List<HomeScreenDestination>
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                for (screen in items) {
                    HomeScreenBottomNavigationItem(
                        screen = screen,
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeScreenNavigationHost(navController, innerPadding)
    }
}

@Composable
fun HomeScreenWithNavigationRail(
    navController: NavHostController,
    items: List<HomeScreenDestination>
) {
    Scaffold { innerPadding ->
        Row(Modifier.padding(innerPadding)) {
            NavigationRail(
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                for (screen in items) {
                    HomeScreenNavigationRailItem(
                        screen = screen,
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }

            HomeScreenNavigationHost(navController, PaddingValues(0.dp))
        }
    }
}

@Composable
fun HomeScreenNavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = DepartureBoardDestination().routeSpecification,
        modifier = Modifier.padding(innerPadding)
    ) {
        for (destination in Destination.allDestinations) {
            composable(
                route = destination.routeSpecification,
                arguments = destination.arguments
            ) { backStackEntry ->
                destination.composable(navController, backStackEntry.arguments)
            }
        }
    }
}

@Composable
fun RowScope.HomeScreenBottomNavigationItem(
    screen: HomeScreenDestination,
    navController: NavController,
    currentDestination: NavDestination?
) {
    BottomNavigationItem(
        icon = { Icon(screen.icon, contentDescription = null) },
        label = {
            Text(
                stringResource(screen.titleRes),
                fontSize = 12.sp
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.destination.routeSpecification
        } == true,
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        onClick = {
            navController.navigate(screen.destination.buildRoute()) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun HomeScreenNavigationRailItem(
    screen: HomeScreenDestination,
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationRailItem(
        icon = { Icon(screen.icon, contentDescription = null) },
        label = {
            Text(
                stringResource(screen.titleRes),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.destination.routeSpecification
        } == true,
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        onClick = {
            navController.navigate(screen.destination.buildRoute()) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}
