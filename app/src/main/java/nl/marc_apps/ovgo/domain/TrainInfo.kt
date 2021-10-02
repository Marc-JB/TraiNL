package nl.marc_apps.ovgo.domain

import android.os.Parcel
import android.os.Parcelable
import nl.marc_apps.ovgo.utils.BitwiseOperations
import nl.marc_apps.ovgo.utils.readParcelable
import nl.marc_apps.ovgo.utils.readTypedList

data class TrainInfo(
    val journeyId: Int,
    val trainParts: List<TrainPart>
): Parcelable {
    val facilities by lazy {
        TrainFacilities(
            trainParts.sumOf { it.facilities.seatsFirstClass },
            trainParts.sumOf { it.facilities.seatsSecondClass },
            trainParts.map { it.facilities.features }.reduce { first, second -> first or second }
        )
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readTypedList(TrainPart.CREATOR)
    )

    data class TrainFacilities(
        val seatsFirstClass: Int = 0,
        val seatsSecondClass: Int = 0,
        val features: Int = 0
    ): Parcelable {
        val hasFirstClass = seatsFirstClass > 0 && seatsSecondClass > 0

        val hasToilet
            get() = BitwiseOperations.getBooleanFromInt(features, 0)

        val hasSilenceCompartment
            get() = BitwiseOperations.getBooleanFromInt(features, 1)

        val hasPowerSockets
            get() = BitwiseOperations.getBooleanFromInt(features, 2)

        val isWheelChairAccessible
            get() = BitwiseOperations.getBooleanFromInt(features, 3)

        val hasBicycleCompartment
            get() = BitwiseOperations.getBooleanFromInt(features, 4)

        val hasFreeWifi
            get() = BitwiseOperations.getBooleanFromInt(features, 5)

        val hasBistro
            get() = BitwiseOperations.getBooleanFromInt(features, 6)

        constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt()
        )

        constructor(
            seatsFirstClass: Int = 0,
            seatsSecondClass: Int = 0,
            hasToilet: Boolean = false,
            hasSilenceCompartment: Boolean = false,
            hasPowerSockets: Boolean = false,
            isWheelChairAccessible: Boolean = false,
            hasBicycleCompartment: Boolean = false,
            hasFreeWifi: Boolean = false,
            hasBistro: Boolean = false
        ): this(
            seatsFirstClass,
            seatsSecondClass,
            BitwiseOperations.constructBooleanInt(
                hasToilet,
                hasSilenceCompartment,
                hasPowerSockets,
                isWheelChairAccessible,
                hasBicycleCompartment,
                hasFreeWifi,
                hasBistro
            )
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(seatsFirstClass)
            parcel.writeInt(seatsSecondClass)
            parcel.writeInt(features)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<TrainFacilities> {
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
        parcel.writeTypedList(trainParts)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<TrainInfo> {
        override fun createFromParcel(parcel: Parcel) = TrainInfo(parcel)

        override fun newArray(size: Int) = arrayOfNulls<TrainInfo>(size)
    }
}
