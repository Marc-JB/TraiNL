package nl.marc_apps.ovgo.ui.departure_board

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.TrainStationDisplayName
import nl.marc_apps.ovgo.ui.components.PlatformView
import nl.marc_apps.ovgo.ui.components.TrainImagesView
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.DeparturePreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.BluePrimary
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

private const val MAX_STATIONS_DISPLAYED_ON_ROUTE = 2

private val DepartureViewSpacingHorizontal = 16.dp
private val DepartureViewItemSpacingVertical = 8.dp

@Composable
fun ActiveDepartureView(departure: Departure, imageLoader: ImageLoader? = null, onDepartureSelected: (Departure) -> Unit) {
    Column(
        modifier = Modifier
            .clickable {
                onDepartureSelected(departure)
            }
            .padding(0.dp, DepartureViewItemSpacingVertical)
    ) {
        Row(
            modifier = Modifier.padding(DepartureViewSpacingHorizontal, 0.dp)
        ) {
            Text(
                if (departure.isDelayed) {
                    stringResource(
                        R.string.departure_time_delayed,
                        departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT),
                        departure.delay.inWholeMinutes
                    )
                } else {
                    departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT)
                },
                style = MaterialTheme.typography.subtitle1,
                color = colorResource(if (departure.isDelayed) R.color.sectionTitleWarningColor else R.color.sectionTitleOkColor),
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            )

            Spacer(Modifier.width(2.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alignByBaseline()
            ) {
                Text(
                    (departure.actualDirection ?: departure.plannedDirection)?.let {
                        TrainStationDisplayName.createDisplayName(it)
                    } ?: "",
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary
                )

                if (departure.stationsOnRoute.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))

                    val upcomingStations = remember(departure.stationsOnRoute) {
                        departure.stationsOnRoute.joinToString(limit = MAX_STATIONS_DISPLAYED_ON_ROUTE) {
                            TrainStationDisplayName.createDisplayName(it)
                        }
                    }

                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            stringResource(R.string.departure_via_stations, upcomingStations),
                            style = MaterialTheme.typography.overline
                        )
                    }
                }
            }

            Spacer(Modifier.width(2.dp))

            Box(
                Modifier
                    .alignByBaseline()
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                PlatformIcon(platform = departure.actualTrack, platformChanged = departure.platformChanged)
            }
        }

        Spacer(Modifier.height(DepartureViewItemSpacingVertical))

        TrainInfoRow(departure, imageLoader)

        for (warning in departure.warnings) {
            Text(
                warning,
                style = MaterialTheme.typography.overline,
                color = colorResource(R.color.departureMessageWarningColor),
                modifier = Modifier.padding(DepartureViewSpacingHorizontal, 2.dp)
            )
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            for (message in departure.messages) {
                Text(
                    message,
                    style = MaterialTheme.typography.overline,
                    modifier = Modifier.padding(DepartureViewSpacingHorizontal, 2.dp)
                )
            }
        }
    }
}

@Composable
fun TrainInfoRow(departure: Departure, imageLoader: ImageLoader? = null) {
    if (departure.trainInfo?.trainParts?.firstOrNull()?.imageUrl == null) {
        return
    }

    val imageUrls = departure.trainInfo.trainParts.mapNotNull { it.imageUrl }

    TrainImagesView(
        imageUrls,
        dimensionResource(R.dimen.train_image_spacing_start),
        DepartureViewSpacingHorizontal,
        imageLoader,
        header = {
            Row {
                Spacer(Modifier.width(DepartureViewSpacingHorizontal))

                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        if (departure.operator == departure.categoryName) {
                            departure.operator
                        } else {
                            stringResource(
                                R.string.departure_operator_and_type_multi_line,
                                departure.operator,
                                departure.categoryName
                            )
                        },
                        style = MaterialTheme.typography.overline
                    )
                }

                Spacer(Modifier.width(4.dp))
            }
        }
    )

    Spacer(Modifier.height(DepartureViewItemSpacingVertical))
}

private val PlatformIconSize = 40.dp
private val PlatformIconWhiteSquareSize = 10.dp

@Composable
fun PlatformIcon(platform: String, platformChanged: Boolean) {
    val platformColor = if(platformChanged) colorResource(R.color.colorError) else BluePrimary

    PlatformView(
        modifier = Modifier
            .size(PlatformIconSize),
        whiteSquareSize = PlatformIconWhiteSquareSize,
        backgroundColor = platformColor,
        contentColor = Color.White
    ) {
        Text(
            platform,
            style = MaterialTheme.typography.body2,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.Bold
        )
    }
}

@DayNightPreview
@Composable
fun ActiveDepartureViewPreview(
    @PreviewParameter(DeparturePreviewParameterProvider::class) departure: Departure
) {
    AppTheme {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            ActiveDepartureView(departure, null) {}
        }
    }
}
