package nl.marc_apps.ovgo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

val DutchRailwaysDisruption.DisruptionOrMaintenance.activeTimeSpan
    get() = timespans.filter {
        it.end != null && Clock.System.now() < it.end
    }.minByOrNull {
        it.start
    }

val DutchRailwaysDisruption.DisruptionOrMaintenance.activeAlternativeTransportTimeSpan
    get() = alternativeTransportTimespans.filter {
        val currentDate = Clock.System.now()
        (it.end == null && it.start != null && currentDate > it.start) || (it.end != null && currentDate < it.end)
    }.minByOrNull {
        it.start ?: Instant.DISTANT_FUTURE
    }

val DutchRailwaysDisruption.DisruptionOrMaintenance.description
    get() = listOfNotNull(
        activeTimeSpan?.situation?.label,
        summaryAdditionalTravelTime?.label ?: activeTimeSpan?.additionalTravelTime?.label,
        expectedDuration?.description,
        activeAlternativeTransportTimeSpan?.alternativeTransport?.label ?: activeTimeSpan?.alternativeTransport?.label
    ).joinToString(separator = " ")

private val DisruptionItemSpacing = 8.dp

@Composable
fun Disruption(
    disruption: DutchRailwaysDisruption.DisruptionOrMaintenance,
    onDisruptionSelected: (DutchRailwaysDisruption.DisruptionOrMaintenance) -> Unit
) {
    Column(
        Modifier
            .clickable {
                onDisruptionSelected(disruption)
            }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            disruption.title,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary
        )

        Spacer(Modifier.height(DisruptionItemSpacing))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                disruption.description,
                style = MaterialTheme.typography.body2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(DisruptionItemSpacing))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(R.drawable.ic_date_range),
                    contentDescription = "Timespan"
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    when {
                        disruption.end == null && Clock.System.now() > disruption.start -> {
                            stringResource(R.string.disruption_end_time_unknown)
                        }
                        disruption.end != null && Clock.System.now() > disruption.start -> {
                            stringResource(
                                R.string.disruption_end_time, disruption.end.format(
                                    DateFormat.MEDIUM, DateFormat.SHORT))
                        }
                        disruption.end == null -> {
                            disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT)
                        }
                        else -> {
                            disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT) + "\n" + disruption.end.format(
                                DateFormat.MEDIUM, DateFormat.SHORT)
                        }
                    },
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}
