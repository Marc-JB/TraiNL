package nl.marc_apps.ovgo.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinInstant
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import java.time.Instant as JtInstant

@Keep
@Parcelize
data class JourneyStop(
    val id: String,
    val station: TrainStation,
    private val _plannedArrivalTime: JtInstant? = null,
    private val _actualArrivalTime: JtInstant? = null,
    private val _plannedDepartureTime: JtInstant? = null,
    private val _actualDepartureTime: JtInstant? = null,
    val plannedTrack: String,
    val actualTrack: String,
    val isCancelled: Boolean = false,
    val punctuality: Int? = null
) : Parcelable {
    @IgnoredOnParcel
    val plannedArrivalTime = _plannedArrivalTime?.toKotlinInstant()

    @IgnoredOnParcel
    val actualArrivalTime = _actualArrivalTime?.toKotlinInstant()

    @IgnoredOnParcel
    val plannedDepartureTime = _plannedDepartureTime?.toKotlinInstant()

    @IgnoredOnParcel
    val actualDepartureTime = _actualDepartureTime?.toKotlinInstant()

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
