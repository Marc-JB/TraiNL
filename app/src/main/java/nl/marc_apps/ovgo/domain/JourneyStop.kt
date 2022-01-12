package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import nl.marc_apps.ovgo.utils.readOrNull
import nl.marc_apps.ovgo.utils.readParcelable
import java.util.*
import java.util.concurrent.TimeUnit

@Keep
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

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readParcelable<TrainStation>()!!,
        parcel.readOrNull<Long>()?.let { Date(it) },
        parcel.readOrNull<Long>()?.let { Date(it) },
        parcel.readOrNull<Long>()?.let { Date(it) },
        parcel.readOrNull<Long>()?.let { Date(it) },
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() == TRUE_AS_BYTE,
        parcel.readOrNull<Int>()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(station, flags)
        parcel.writeValue(plannedArrivalTime?.time)
        parcel.writeValue(actualArrivalTime?.time)
        parcel.writeValue(plannedDepartureTime?.time)
        parcel.writeValue(actualDepartureTime?.time)
        parcel.writeString(plannedTrack)
        parcel.writeString(actualTrack)
        parcel.writeByte(if (isCancelled) TRUE_AS_BYTE else FALSE_AS_BYTE)
        parcel.writeValue(punctuality)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<JourneyStop> {
        private const val FALSE_AS_BYTE = 0.toByte()

        private const val TRUE_AS_BYTE = 1.toByte()

        override fun createFromParcel(parcel: Parcel) = JourneyStop(parcel)

        override fun newArray(size: Int) = arrayOfNulls<JourneyStop>(size)
    }
}
