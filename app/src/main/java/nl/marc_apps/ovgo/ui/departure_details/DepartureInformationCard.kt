package nl.marc_apps.ovgo.ui.departure_details

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.theme.AppTheme
import nl.marc_apps.ovgo.ui.theme.Card
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DepartureInformationCard(departure: Departure) {
    Card {
        Text(stringResource(R.string.departure_information), style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(8.dp))

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                if (departure.isDelayed) {
                    pluralStringResource(
                        R.plurals.departure_time_long_text_delayed,
                        departure.delay.inWholeMinutes.toInt(),
                        departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT),
                        departure.delay.inWholeMinutes
                    )
                } else {
                    stringResource(
                        R.string.departure_time_long_text_no_delay,
                        departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT)
                    )
                },
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                DateUtils.getRelativeTimeSpanString(
                    departure.actualDepartureTime.toEpochMilliseconds(),
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                ).toString(),
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(stringResource(R.string.platform, departure.actualTrack), style = MaterialTheme.typography.body2)
        }
    }
}

@Preview
@Composable
fun DepartureInformationCardPreview() {
    AppTheme {
        Surface(color = MaterialTheme.colors.background) {
            DepartureInformationCard(
                Departure(
                    "abcd",
                    _plannedDepartureTime = (Clock.System.now() + 2.minutes).toJavaInstant(),
                    _actualDepartureTime = (Clock.System.now() + 4.minutes).toJavaInstant(),
                    plannedTrack = "4b",
                    actualTrack = "4",
                    operator = "NS",
                    categoryName = "Sprinter"
                )
            )
        }
    }
}
