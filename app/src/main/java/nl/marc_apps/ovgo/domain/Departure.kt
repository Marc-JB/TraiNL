package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import nl.marc_apps.ovgo.utils.readParcelable
import nl.marc_apps.ovgo.utils.readStringCollection
import nl.marc_apps.ovgo.utils.readTypedList
import java.util.*
import java.util.concurrent.TimeUnit

@Keep
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

    constructor(parcel: Parcel) : this(
        parcel.readInt().toString(),
        parcel.readParcelable<TrainStation>(),
        parcel.readParcelable<TrainStation>(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable<TrainInfo>(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != FALSE_AS_BYTE,
        parcel.readTypedList(TrainStation.CREATOR),
        parcel.readStringCollection().toSet(),
        parcel.readStringCollection().toSet(),
        parcel.readByte() == TRUE_AS_BYTE
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(journeyId.toInt())
        parcel.writeParcelable(actualDirection, flags)
        parcel.writeParcelable(plannedDirection, flags)
        parcel.writeLong(plannedDepartureTime.time)
        parcel.writeLong(actualDepartureTime.time)
        parcel.writeString(plannedTrack)
        parcel.writeString(actualTrack)
        parcel.writeParcelable(trainInfo, flags)
        parcel.writeString(operator)
        parcel.writeString(categoryName)
        parcel.writeByte(if (isCancelled) TRUE_AS_BYTE else FALSE_AS_BYTE)
        parcel.writeTypedList(stationsOnRoute)
        parcel.writeStringList(messages.toList())
        parcel.writeStringList(warnings.toList())
        parcel.writeByte(if(isForeignService) TRUE_AS_BYTE else FALSE_AS_BYTE)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Departure> {
        private const val FALSE_AS_BYTE = 0.toByte()

        private const val TRUE_AS_BYTE = 1.toByte()

        override fun createFromParcel(parcel: Parcel) = Departure(parcel)

        override fun newArray(size: Int) = arrayOfNulls<Departure>(size)
    }
}
