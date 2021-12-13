package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import nl.marc_apps.ovgo.utils.readStringCollection

@Keep
data class TrainStation(
    val uicCode: String,
    val fullName: String,
    val shortenedName: String,
    val alternativeNames: Set<String>,
    val alternativeSearches: Set<String>,
    val hasDepartureTimesBoard: Boolean,
    val hasTravelAssistance: Boolean,
    val country: Country
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readStringCollection().toSet(),
        parcel.readStringCollection().toSet(),
        parcel.readByte() == TRUE_AS_BYTE,
        parcel.readByte() == TRUE_AS_BYTE,
        Country.valueOf(parcel.readString()!!)
    )

    enum class Country(val flag: String) {
        AUSTRIA("\uD83C\uDDE6\uD83C\uDDF9"),
        BELGIUM("\uD83C\uDDE7\uD83C\uDDEA"),
        SWITZERLAND("\uD83C\uDDE8\uD83C\uDDED"),
        GERMANY("\uD83C\uDDE9\uD83C\uDDEA"),
        FRANCE("\uD83C\uDDEB\uD83C\uDDF7"),
        GREAT_BRITAIN("\uD83C\uDDEC\uD83C\uDDE7"),
        THE_NETHERLANDS("\uD83C\uDDF3\uD83C\uDDF1")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uicCode)
        parcel.writeString(fullName)
        parcel.writeString(shortenedName)
        parcel.writeStringList(alternativeNames.toList())
        parcel.writeStringList(alternativeSearches.toList())
        parcel.writeByte(if (hasDepartureTimesBoard) TRUE_AS_BYTE else FALSE_AS_BYTE)
        parcel.writeByte(if (hasTravelAssistance) TRUE_AS_BYTE else FALSE_AS_BYTE)
        parcel.writeString(country.name)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TrainStation> {
        private const val FALSE_AS_BYTE = 0.toByte()

        private const val TRUE_AS_BYTE = 1.toByte()

        override fun createFromParcel(parcel: Parcel) = TrainStation(parcel)

        override fun newArray(size: Int) = arrayOfNulls<TrainStation>(size)
    }
}
