package nl.marc_apps.ovgo.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DepartureBoard
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
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

@DayNightPreview
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
            val layoutState = rememberLayoutState(windowConstraints = this)

            if (layoutState.isLandscape) {
                HomeScreenWithNavigationRail(navController, items, layoutState)
            } else {
                HomeScreenWithBottomNavigation(navController, items, layoutState)
            }
        }
    }
}

@Composable
fun HomeScreenWithBottomNavigation(
    navController: NavHostController,
    items: List<HomeScreenDestination>,
    layoutState: LayoutState
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
        HomeScreenNavigationHost(navController, innerPadding, layoutState)
    }
}

@Composable
fun HomeScreenWithNavigationRail(
    navController: NavHostController,
    items: List<HomeScreenDestination>,
    layoutState: LayoutState
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

            HomeScreenNavigationHost(navController, PaddingValues(0.dp), layoutState)
        }
    }
}

@Composable
fun HomeScreenNavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    layoutState: LayoutState
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
                destination.composable(navController, layoutState, backStackEntry.arguments)
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
