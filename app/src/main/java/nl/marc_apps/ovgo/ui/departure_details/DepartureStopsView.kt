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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import kotlin.time.Duration

@Composable
fun DepartureStopsView(stops: Array<JourneyStop>, navController: NavController?) {
    DepartureStopsView(stops.toList()) {
        val action = DepartureStopsFragmentDirections
            .actionDepartureStopsToStationDepartureBoard(it)
        navController?.navigate(action)
    }
}

private fun isForeignStation(trainStation: TrainStation): Boolean {
    return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DepartureStopsView(stops: List<JourneyStop>, onStationSelected: (TrainStation) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Pill()

        Spacer(Modifier.height(16.dp))

        val nestedScrollInterop = rememberNestedScrollInteropConnection()

        LazyColumn(modifier = Modifier.nestedScroll(nestedScrollInterop)) {
            items(stops, key = { it.id }) {
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
                            onStationSelected(it.station)
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
                        val hasSingleTime = it.actualArrivalTime == it.actualDepartureTime && !it.isDepartureDelayed && !it.isArrivalDelayed
                        if (!hasSingleTime && it.plannedArrivalTime != null) {
                            TimeDisplayView(it.plannedArrivalTime, it.arrivalDelay)
                            Spacer(Modifier.width(2.dp))
                        }

                        if (it.plannedDepartureTime != null) {
                            Spacer(Modifier.width(2.dp))
                            TimeDisplayView(it.plannedDepartureTime, it.departureDelay)
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        buildString {
                            append(it.station.fullName)

                            if (isForeignStation(it.station)) {
                                append(" ")
                                append(it.station.country?.flag)
                            }
                        },
                        color = colorResource(if (it.isCancelled) R.color.sectionTitleWarningColor else R.color.sectionTitleColor),
                        style = MaterialTheme.typography.subtitle1
                    )
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
        style = MaterialTheme.typography.subtitle1
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DepartureStopsViewPreview() {
    val stops = listOf(
        JourneyStop(
            "abcd",
            TrainStation("ddr", "Dordrecht", "Dordrecht"),
            null,
            null,
            Instant.parse("2022-07-18T10:20Z").toJavaInstant(),
            Instant.parse("2022-07-18T10:20Z").toJavaInstant(),
            "3a",
            "3a"
        ),
        JourneyStop(
            "efgh",
            TrainStation("rtd", "Rotterdam Centraal", "Rotterdam", setOf("Rotterdam CS", "Rotterdam")),
            Instant.parse("2022-07-18T10:40Z").toJavaInstant(),
            Instant.parse("2022-07-18T10:40Z").toJavaInstant(),
            Instant.parse("2022-07-18T10:43Z").toJavaInstant(),
            Instant.parse("2022-07-18T10:44Z").toJavaInstant(),
            "6",
            "7"
        ),
        JourneyStop(
            "ijkl",
            TrainStation("gvc", "Den Haag Centraal", "Den Haag", setOf("Den Haag CS", "Den Haag")),
            Instant.parse("2022-07-18T10:40Z").toJavaInstant(),
            Instant.parse("2022-07-18T10:40Z").toJavaInstant(),
            null,
            null,
            "20",
            "20"
        )
    )
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DepartureStopsView(stops) {}
        }
    }
}
