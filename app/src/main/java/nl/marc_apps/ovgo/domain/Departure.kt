package nl.marc_apps.ovgo.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*
import java.util.concurrent.TimeUnit

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

    private val delayInMs: Long
        get() = actualDepartureTime.time - plannedDepartureTime.time

    val delayInMinutesRounded
        get() = TimeUnit.MINUTES.convert(delayInMs, TimeUnit.MILLISECONDS).toInt()

    val isDelayed
        get() = delayInMinutesRounded > 0
}
