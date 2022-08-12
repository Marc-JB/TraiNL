package nl.marc_apps.ovgo.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import nl.marc_apps.ovgo.ui.departure_board.DepartureBoardView
import nl.marc_apps.ovgo.ui.departure_details.DepartureDetailsView
import nl.marc_apps.ovgo.ui.disruptions.DisruptionsView
import nl.marc_apps.ovgo.ui.maintenance.MaintenanceView
import nl.marc_apps.ovgo.ui.search_station.SearchStationView

sealed class Destination(
    val routeSpecification: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val composable: @Composable (NavHostController, Bundle?) -> Unit
) {
    companion object {
        val allDestinations = listOf(
            DepartureBoardDestination,
            DisruptionsDestination,
            MaintenanceDestination,
            DepartureDetailsDestination,
            StationSearchDestination
        )
    }
}

private const val PARAM_STATION_ID = "stationId"

private const val PARAM_DEPARTURE_ID = "departureId"

object DepartureBoardDestination : Destination(
    routeSpecification = "departure_board?$PARAM_STATION_ID={$PARAM_STATION_ID}",
    arguments = listOf(
        navArgument(PARAM_STATION_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
    ),
    composable = { navController, arguments ->
        DepartureBoardView(
            stationId = arguments?.getString(PARAM_STATION_ID),
            navController = navController
        )
    }
) {

    fun buildRoute(stationId: String? = null): String {
        return if (stationId != null) "departure_board?$PARAM_STATION_ID=$stationId" else "departure_board"
    }
}

object DisruptionsDestination : Destination(
    routeSpecification = "disruptions",
    composable = { _, _ -> DisruptionsView() }
) {
    fun buildRoute() = routeSpecification
}

object MaintenanceDestination : Destination(
    routeSpecification = "maintenance",
    composable = { _, _ -> MaintenanceView() }
) {
    fun buildRoute() = routeSpecification
}

object DepartureDetailsDestination : Destination(
    routeSpecification = "departures/{$PARAM_DEPARTURE_ID}",
    arguments = listOf(
        navArgument(PARAM_DEPARTURE_ID) {
            type = NavType.StringType
            nullable = false
        }
    ),
    composable = { navController, arguments ->
        DepartureDetailsView(
            departureId = arguments?.getString(PARAM_DEPARTURE_ID),
            navController = navController
        )
    }
) {
    fun buildRoute(departureId: String) = "departures/${departureId}"
}

object StationSearchDestination : Destination(
    routeSpecification = "station_search",
    composable = { navController, _ ->
        SearchStationView(navController = navController)
    }
) {
    fun buildRoute() = routeSpecification
}
