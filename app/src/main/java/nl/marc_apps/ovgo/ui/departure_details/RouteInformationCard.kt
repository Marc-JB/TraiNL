package nl.marc_apps.ovgo.ui.departure_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.components.Card
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme

@Composable
fun RouteInformationCard(
    departure: Departure,
    journeyStops: List<JourneyStop>?,
    onStationSelected: (TrainStation) -> Unit,
    showAllStops: () -> Unit
) {
    Card(applyHorizontalPadding = false) { padding: Dp ->
        Text(
            stringResource(R.string.departure_information),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(padding, 0.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val direction = departure.actualDirection

        if (direction != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(padding, 0.dp)
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(stringResource(R.string.direction), style = MaterialTheme.typography.body2)
                }

                Spacer(modifier = Modifier.width(8.dp))

                StationChip(direction) {
                    onStationSelected(direction)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (departure.stationsOnRoute.isNotEmpty()) {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Spacer(Modifier.width(padding))

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            stringResource(R.string.departure_via),
                            style = MaterialTheme.typography.body2
                        )
                    }

                    Spacer(Modifier.width(8.dp))
                }

                items(departure.stationsOnRoute) {
                    StationChip(it) {
                        onStationSelected(it)
                    }

                    Spacer(
                        Modifier.width(
                            if (it == departure.stationsOnRoute.last()) {
                                padding
                            } else {
                                8.dp
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            enabled = !journeyStops.isNullOrEmpty(),
            onClick = { showAllStops() },
            modifier = Modifier.padding(padding, 0.dp)
        ) {
            if (journeyStops == null) {
                CircularProgressIndicator()

                Spacer(Modifier.width(4.dp))
            }

            Text(stringResource(R.string.show_all_stops))
        }
    }
}

private fun isForeignStation(trainStation: TrainStation): Boolean {
    return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StationChip(station: TrainStation, onSelected: () -> Unit) {
    Chip(
        onClick = { onSelected() }
    ) {
        Text(buildString {
            append(station.fullName)

            if (isForeignStation(station)) {
                append(" ")
                append(station.country?.flag)
            }
        })
    }
}

@DayNightPreview
@Composable
fun RouteInformationCardPreview(
    @PreviewParameter(DeparturePreviewParameterProvider::class) departure: Departure
) {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            RouteInformationCard(departure, null, {}, {})
        }
    }
}
