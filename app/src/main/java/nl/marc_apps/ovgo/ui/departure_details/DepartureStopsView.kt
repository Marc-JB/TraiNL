package nl.marc_apps.ovgo.ui.departure_details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.preview.DayNightPreview
import nl.marc_apps.ovgo.ui.preview.fixtures.JourneyStopPreviewParameterProvider
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import kotlin.time.Duration

private fun isForeignStation(trainStation: TrainStation): Boolean {
    return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
}

@Composable
fun DepartureStopsView(stops: List<JourneyStop>, onStationSelected: (TrainStation) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Pill()

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(stops, key = { it.id }) {
                DepartureStop(it) {
                    onStationSelected(it.station)
                }

                if (it != stops.last()) {
                    Divider(
                        modifier = Modifier.padding(16.dp, 0.dp)
                    )
                }
            }

            val spaceToInsert = maxOf(5 - stops.size, 0)

            items(spaceToInsert, key = { "space-$it" }) {
                Spacer(Modifier.height(56.dp))
            }
        }
    }
}

@Composable
fun DepartureStop(stop: JourneyStop, onSelected: () -> Unit) {
    val intrinsics = ParagraphIntrinsics(
        "00:00 +00",
        style = MaterialTheme.typography.subtitle1,
        density = LocalDensity.current,
        fontFamilyResolver = createFontFamilyResolver(LocalContext.current)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onSelected()
            }
            .heightIn(min = 56.dp)
            .padding(16.dp, 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier.widthIn(min = with(LocalDensity.current) {
                intrinsics.maxIntrinsicWidth.toDp()
            })
        ) {
            val hasSingleTime = stop.actualArrivalTime == stop.actualDepartureTime
                    && stop.plannedArrivalTime == stop.plannedDepartureTime

            if (!hasSingleTime && stop.plannedArrivalTime != null) {
                TimeDisplayView(stop.plannedArrivalTime, stop.arrivalDelay)
                Spacer(Modifier.width(2.dp))
            }

            if (stop.plannedDepartureTime != null) {
                Spacer(Modifier.width(2.dp))
                TimeDisplayView(stop.plannedDepartureTime, stop.departureDelay)
            }
        }

        Spacer(Modifier.width(8.dp))

        Text(
            buildString {
                append(stop.station.fullName)

                if (isForeignStation(stop.station)) {
                    append(" ")
                    append(stop.station.country?.flag)
                }
            },
            color = colorResource(if (stop.isCancelled) R.color.sectionTitleWarningColor else R.color.sectionTitleColor),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun TimeDisplayView(plannedTime: Instant, delay: Duration) {
    val delayInMinutesRounded = delay.inWholeMinutes
    val isDelayed = delayInMinutesRounded > 0

    val departureTimeWithDelayText = if (isDelayed) {
        stringResource(
            R.string.departure_time_delayed,
            plannedTime.format(timeStyle = DateFormat.SHORT),
            delayInMinutesRounded
        )
    } else {
        plannedTime.format(timeStyle = DateFormat.SHORT)
    }

    Text(
        departureTimeWithDelayText,
        color = colorResource(if(isDelayed) R.color.sectionTitleWarningColor else R.color.sectionTitleOkColor),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.testTag("TimeDisplayView")
    )
}

@Composable
fun Pill() {
    Box(
        modifier = Modifier
            .size(32.dp, 8.dp)
            .clip(shape = RoundedCornerShape(4.dp))
            .background(colorResource(R.color.bottomSheetPillColor))
    ) {}
}

@DayNightPreview
@Composable
fun DepartureStopsViewPreview() {
    val stops = JourneyStopPreviewParameterProvider().values.toList()
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DepartureStopsView(stops) {}
        }
    }
}
