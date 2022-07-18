package nl.marc_apps.ovgo.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.datetime.toKotlinInstant
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration
import java.time.Instant as JtInstant

@Keep
@Parcelize
data class Departure(
    val journeyId: String,
    val actualDirection: TrainStation? = null,
    val plannedDirection: TrainStation? = null,
    private val _plannedDepartureTime: JtInstant,
    private val _actualDepartureTime: JtInstant,
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
    @IgnoredOnParcel
    val plannedDepartureTime = _plannedDepartureTime.toKotlinInstant()

    @IgnoredOnParcel
    val actualDepartureTime = _actualDepartureTime.toKotlinInstant()

    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val delay: Duration
        get() = actualDepartureTime - plannedDepartureTime

    val isDelayed
        get() = delay.inWholeMinutes > 0
}
