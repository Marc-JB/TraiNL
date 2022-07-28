package nl.marc_apps.ovgo.ui.preview.fixtures

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.domain.Departure
import kotlin.time.Duration.Companion.minutes

class DeparturePreviewParameterProvider : PreviewParameterProvider<Departure> {
    override val values = sequenceOf(
        Departure(
            "abcd",
            actualDirection = TrainStationPreviewParameterProvider().values.last(),
            _plannedDepartureTime = (Clock.System.now() + 2.minutes).toJavaInstant(),
            _actualDepartureTime = (Clock.System.now() + 4.minutes).toJavaInstant(),
            plannedTrack = "4b",
            actualTrack = "4",
            operator = "NS",
            categoryName = "Sprinter",
            stationsOnRoute = TrainStationPreviewParameterProvider().values.toList(),
            warnings = setOf("Ends at Rotterdam Centraal"),
            messages = setOf("Does not stop in Rotterdam Kralingse Zoom")
        )
    )
}
