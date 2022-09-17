package nl.marc_apps.ovgo.domain

import androidx.annotation.Keep
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import java.time.Instant as JtInstant

@Keep
data class JourneyStop(
    val id: String,
    val station: TrainStation,
    val plannedArrivalTime: Instant? = null,
    val actualArrivalTime: Instant? = null,
    val plannedDepartureTime: Instant? = null,
    val actualDepartureTime: Instant? = null,
    val plannedTrack: String,
    val actualTrack: String,
    val isCancelled: Boolean = false,
    val punctuality: Int? = null
) {
    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val arrivalDelay: Duration
        get() = if (actualArrivalTime == null || plannedArrivalTime == null) {
            0.milliseconds
        } else {
            actualArrivalTime - plannedArrivalTime
        }

    val departureDelay: Duration
        get() = if (actualDepartureTime == null || plannedDepartureTime == null) {
            0.milliseconds
        } else {
            actualDepartureTime - plannedDepartureTime
        }

    val isDepartureDelayed
        get() = departureDelay.inWholeMinutes > 0

    val isArrivalDelayed
        get() = arrivalDelay.inWholeMinutes > 0

    val isOnStation: Boolean
        get() = (actualDepartureTime == null || actualDepartureTime > Clock.System.now())
                && (actualArrivalTime == null || actualArrivalTime < Clock.System.now())
}
