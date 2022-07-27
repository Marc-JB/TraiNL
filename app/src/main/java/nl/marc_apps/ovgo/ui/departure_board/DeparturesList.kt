package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.booleanResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure

@Composable
fun DeparturesList(
    departureBoardViewModel: DepartureBoardViewModel,
    navController: NavController,
    imageLoader: ImageLoader
) {
    val departures by departureBoardViewModel.departures.observeAsState()

    departures?.let {
        if (it.getOrThrow().isNotEmpty()) {
            DeparturesList(it.getOrThrow(), imageLoader) {
                val action = DepartureBoardFragmentDirections.actionDepartureBoardToDetails(it)
                navController.navigate(action)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DeparturesList(departures: List<Departure>, imageLoader: ImageLoader? = null, onDepartureSelected: (Departure) -> Unit) {
    val nestedScrollInterop = rememberNestedScrollInteropConnection()

    if (booleanResource(R.bool.is_large_screen_device)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().nestedScroll(nestedScrollInterop)
        ) {
            itemsIndexed(
                departures,
                key = { _, item -> item.journeyId },
                contentType = { _, item -> item.isCancelled }
            ) { index, departure ->
                DepartureView(departure, imageLoader, onDepartureSelected)

                // TODO: Vertical divider

                if (index != departures.lastIndex || index != departures.lastIndex - 1) {
                    Divider(
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().nestedScroll(nestedScrollInterop)
        ) {
            itemsIndexed(
                departures,
                key = { _, item -> item.journeyId },
                contentType = { _, item -> item.isCancelled }
            ) { index, departure ->
                DepartureView(departure, imageLoader, onDepartureSelected)

                if (index != departures.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DepartureView(departure: Departure, imageLoader: ImageLoader? = null, onDepartureSelected: (Departure) -> Unit) {
    if (departure.isCancelled) {
        CancelledDepartureView(departure)
    } else {
        ActiveDepartureView(departure, imageLoader, onDepartureSelected)
    }
}
