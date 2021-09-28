package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import nl.marc_apps.ovgo.utils.readStringCollection

data class TrainStation(
    val uicCode: String,
    val name: String,
    val alternativeNames: Set<String>,
    val alternativeSearches: Set<String>,
    val hasDepartureTimesBoard: Boolean,
    val hasTravelAssistance: Boolean,
    val country: Country
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readStringCollection().toSet(),
        parcel.readStringCollection().toSet(),
        parcel.readByte() == TRUE_AS_BYTE,
        parcel.readByte() == TRUE_AS_BYTE,
        Country.valueOf(parcel.readString()!!)
    )

    enum class Country {
        AUSTRIA,
        BELGIUM,
        SWITZERLAND,
        GERMANY,
        FRANCE,
        GREAT_BRITAIN,
        THE_NETHERLANDS
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uicCode)
        parcel.writeString(name)
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
