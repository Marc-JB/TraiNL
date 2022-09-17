package nl.marc_apps.ovgo.domain

import androidx.annotation.Keep
import kotlinx.datetime.Instant
import kotlin.time.Duration

@Keep
data class Departure(
    val journeyId: String,
    val actualDirection: TrainStation? = null,
    val plannedDirection: TrainStation? = null,
    val plannedDepartureTime: Instant,
    val actualDepartureTime: Instant,
    val plannedTrack: String,
    val actualTrack: String,
    val trainInfo: TrainInfo? = null,
    val operator: String,
    val categoryName: String,
    val isCancelled: Boolean = false,
    val stationsOnRoute: List<TrainStation> = emptyList(),
    val messages: Set<String> = emptySet(),
    val warnings: Set<String> = emptySet(),
    val isForeignService: Boolean = false
) {
    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val delay: Duration
        get() = actualDepartureTime - plannedDepartureTime

    val isDelayed
        get() = delay.inWholeMinutes > 0
}
