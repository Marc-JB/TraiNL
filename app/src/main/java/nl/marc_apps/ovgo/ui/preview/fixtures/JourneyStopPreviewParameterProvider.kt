package nl.marc_apps.ovgo.ui.preview.fixtures

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import nl.marc_apps.ovgo.domain.JourneyStop
import kotlin.time.Duration.Companion.minutes

class JourneyStopPreviewParameterProvider : PreviewParameterProvider<JourneyStop> {
    override val values = sequenceOf(
        JourneyStop(
            "abcd",
            TrainStationPreviewParameterProvider().values.elementAt(0),
            null,
            null,
            (Clock.System.now() + 2.minutes).toJavaInstant(),
            (Clock.System.now() + 2.minutes).toJavaInstant(),
            "3a",
            "3a"
        ),
        JourneyStop(
            "efgh",
            TrainStationPreviewParameterProvider().values.elementAt(1),
            (Clock.System.now() + 22.minutes).toJavaInstant(),
            (Clock.System.now() + 22.minutes).toJavaInstant(),
            (Clock.System.now() + 24.minutes).toJavaInstant(),
            (Clock.System.now() + 25.minutes).toJavaInstant(),
            "6",
            "7"
        ),
        JourneyStop(
            "ijkl",
            TrainStationPreviewParameterProvider().values.elementAt(2),
            (Clock.System.now() + 44.minutes).toJavaInstant(),
            (Clock.System.now() + 44.minutes).toJavaInstant(),
            null,
            null,
            "20",
            "20"
        )
    )
}
