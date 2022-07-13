package nl.marc_apps.ovgo.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Keep
@Parcelize
data class Departure(
    val journeyId: String,
    val actualDirection: TrainStation? = null,
    val plannedDirection: TrainStation? = null,
    val plannedDepartureTime: Date,
    val actualDepartureTime: Date,
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
): Parcelable {
    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val delay: Duration
        get() = (actualDepartureTime.time - plannedDepartureTime.time).milliseconds

    val isDelayed
        get() = delay.inWholeMinutes > 0
}
