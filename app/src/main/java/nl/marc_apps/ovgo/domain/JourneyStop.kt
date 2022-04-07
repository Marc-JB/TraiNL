package nl.marc_apps.ovgo.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit

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

    private val arrivalDelayInMs: Long
        get() = if (actualArrivalTime == null || plannedArrivalTime == null) {
            0
        } else {
            actualArrivalTime.time - plannedArrivalTime.time
        }

    val arrivalDelayInMinutesRounded
        get() = TimeUnit.MINUTES.convert(arrivalDelayInMs, TimeUnit.MILLISECONDS).toInt()

    private val departureDelayInMs: Long
        get() = if (actualDepartureTime == null || plannedDepartureTime == null) {
            0
        } else {
            actualDepartureTime.time - plannedDepartureTime.time
        }

    val departureDelayInMinutesRounded
        get() = TimeUnit.MINUTES.convert(departureDelayInMs, TimeUnit.MILLISECONDS).toInt()

    val isDepartureDelayed
        get() = departureDelayInMinutesRounded > 0

    val isArrivalDelayed
        get() = arrivalDelayInMinutesRounded > 0

    val isOnStation: Boolean
        get() = (actualDepartureTime == null || actualDepartureTime.before(Date()))
                && (actualArrivalTime == null || actualArrivalTime.after(Date()))
}
