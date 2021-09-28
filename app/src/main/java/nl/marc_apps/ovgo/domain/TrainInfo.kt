package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import nl.marc_apps.ovgo.utils.readParcelable
import nl.marc_apps.ovgo.utils.readTypedList

data class TrainInfo(
    val journeyId: Int,
    val trainParts: Collection<TrainPart>
): Parcelable {
    val facilities by lazy {
        TrainFacilities(
            trainParts.sumOf { it.facilities.seatsFirstClass },
            trainParts.sumOf { it.facilities.seatsSecondClass },
            trainParts.any { it.facilities.hasToilet },
            trainParts.any { it.facilities.hasSilenceCompartment },
            trainParts.any { it.facilities.hasPowerSockets },
            trainParts.any { it.facilities.isWheelChairAccessible },
            trainParts.any { it.facilities.hasBicycleCompartment },
            trainParts.any { it.facilities.hasFreeWifi },
            trainParts.any { it.facilities.hasBistro },
        )
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readTypedList(TrainPart.CREATOR)
    )

    data class TrainFacilities(
        val seatsFirstClass: Int = 0,
        val seatsSecondClass: Int = 0,
        val hasToilet: Boolean = false,
        val hasSilenceCompartment: Boolean = false,
        val hasPowerSockets: Boolean = false,
        val isWheelChairAccessible: Boolean = false,
        val hasBicycleCompartment: Boolean = false,
        val hasFreeWifi: Boolean = false,
        val hasBistro: Boolean = false
    ): Parcelable {
        val hasFirstClass = seatsFirstClass > 0 && seatsSecondClass > 0

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE,
            parcel.readByte() == TRUE_AS_BYTE
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(seatsFirstClass)
            parcel.writeInt(seatsSecondClass)
            parcel.writeByte(if (hasToilet) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (hasSilenceCompartment) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (hasPowerSockets) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (isWheelChairAccessible) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (hasBicycleCompartment) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (hasFreeWifi) TRUE_AS_BYTE else FALSE_AS_BYTE)
            parcel.writeByte(if (hasBistro) TRUE_AS_BYTE else FALSE_AS_BYTE)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<TrainFacilities> {
            private const val FALSE_AS_BYTE = 0.toByte()

            private const val TRUE_AS_BYTE = 1.toByte()

            override fun createFromParcel(parcel: Parcel) = TrainFacilities(parcel)

            override fun newArray(size: Int) = arrayOfNulls<TrainFacilities>(size)
        }
    }

    data class TrainPart(
        val id: Int? = null,
        val facilities: TrainFacilities,
        val imageUrl: String? = null
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readParcelable() ?: TrainFacilities(),
            parcel.readString()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(id)
            parcel.writeParcelable(facilities, flags)
            parcel.writeString(imageUrl)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<TrainPart> {
            override fun createFromParcel(parcel: Parcel) = TrainPart(parcel)

            override fun newArray(size: Int) = arrayOfNulls<TrainPart>(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(journeyId)
        parcel.writeTypedList(trainParts.toList())
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TrainInfo> {
        override fun createFromParcel(parcel: Parcel) = TrainInfo(parcel)

        override fun newArray(size: Int) = arrayOfNulls<TrainInfo>(size)
    }
}
