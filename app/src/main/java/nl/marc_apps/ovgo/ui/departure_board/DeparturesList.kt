package nl.marc_apps.ovgo.ui.departure_board

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme

private val DepartureViewSpacingHorizontal = 16.dp

@Composable
fun DeparturesList(
    departures: List<Departure>,
    imageLoader: ImageLoader? = null,
    onDepartureSelected: (Departure) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            departures,
            key = { _, item -> item.journeyId },
            contentType = { _, item -> item.isCancelled }
        ) { index, departure ->
            DepartureView(departure, imageLoader, onDepartureSelected)

            if (index != departures.lastIndex) {
                Divider(
                    modifier = Modifier.padding(DepartureViewSpacingHorizontal, 0.dp)
                )
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

@DayNightPreview
@Composable
fun DeparturesListPreview() {
    val departures = DeparturePreviewParameterProvider().values.toList().mapIndexed { index, departure ->
        if (index == 0) {
            departure.copy(isCancelled = true)
        } else departure
    }

    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DeparturesList(departures, null) {}
        }
    }
}
