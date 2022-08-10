package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.components.PlaceholderImage
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

private val SnackbarSpacing = 16.dp

@Composable
fun DepartureBoardView(
    station: TrainStation? = null,
    departureBoardViewModel: DepartureBoardViewModel = getViewModel(),
    navController: NavController,
    imageLoader: ImageLoader = get()
) {
    val selectedStation by departureBoardViewModel.currentStation.collectAsState()
    val departuresState = departureBoardViewModel.departures.collectAsState()
    val departures = departuresState.value

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StationAppBar(selectedStation) {
            val action = DepartureBoardFragmentDirections.actionHomeToStationSearch()
            navController.navigate(action)
        }

        when {
            departures == null -> {
                Spacer(Modifier.height(24.dp))

                CircularProgressIndicator()
            }
            departures.isFailure -> {
                Spacer(Modifier.weight(1f))

                Snackbar(
                    modifier = Modifier.padding(SnackbarSpacing),
                    actionOnNewLine = true,
                    action = {
                        TextButton(onClick = {
                            departureBoardViewModel.reload()
                        }) {
                            Text(stringResource(R.string.action_retry_loading))
                        }
                    }
                ) {
                    Text(stringResource(R.string.departure_board_loading_failure))
                }
            }
            departures.getOrNull()?.isEmpty() == true -> PlaceholderImage(
                text = stringResource(R.string.no_departures),
                imageId = R.drawable.va_stranded_traveler
            )
            else -> DeparturesList(departures.getOrThrow(), imageLoader) {
                val action = DepartureBoardFragmentDirections.actionDepartureBoardToDetails(it)
                navController.navigate(action)
            }
        }
    }

    LaunchedEffect(station?.uicCode) {
        if (station == null) {
            departureBoardViewModel.loadDeparturesForLastKnownStation()
        } else {
            departureBoardViewModel.loadDepartures(station)
        }
    }
}
