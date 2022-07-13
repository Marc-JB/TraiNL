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
data class JourneyStop(
    val id: String,
    val station: TrainStation,
    val plannedArrivalTime: Date? = null,
    val actualArrivalTime: Date? = null,
    val plannedDepartureTime: Date? = null,
    val actualDepartureTime: Date? = null,
    val plannedTrack: String,
    val actualTrack: String,
    val isCancelled: Boolean = false,
    val punctuality: Int? = null
) : Parcelable {
    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val arrivalDelay: Duration
        get() = if (actualArrivalTime == null || plannedArrivalTime == null) {
            0
        } else {
            actualArrivalTime.time - plannedArrivalTime.time
        }.milliseconds

    val departureDelay: Duration
        get() = if (actualDepartureTime == null || plannedDepartureTime == null) {
            0
        } else {
            actualDepartureTime.time - plannedDepartureTime.time
        }.milliseconds

    val isDepartureDelayed
        get() = departureDelay.inWholeMinutes > 0

    val isArrivalDelayed
        get() = arrivalDelay.inWholeMinutes > 0

    val isOnStation: Boolean
        get() = (actualDepartureTime == null || actualDepartureTime.before(Date()))
                && (actualArrivalTime == null || actualArrivalTime.after(Date()))
}
