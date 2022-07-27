package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader

@Composable
fun DepartureBoardView(
    departureBoardViewModel: DepartureBoardViewModel,
    navController: NavController,
    imageLoader: ImageLoader
) {
    val departuresState = departureBoardViewModel.departures.collectAsState()
    val departures = departuresState.value

    when {
        departures == null -> {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                CircularProgressIndicator()
            }
        }
        departures.getOrNull()?.isNotEmpty() == true -> {
            DeparturesList(departures.getOrThrow(), imageLoader) {
                val action = DepartureBoardFragmentDirections.actionDepartureBoardToDetails(it)
                navController.navigate(action)
            }
        }
    }
}
