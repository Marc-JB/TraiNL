package nl.marc_apps.ovgo.domain

import nl.marc_apps.ovgo.utils.BitwiseOperations

data class TrainInfo(
    val journeyId: Int,
    val trainParts: List<TrainPart>
) {
    val facilities by lazy {
        TrainFacilities(
            trainParts.sumOf { it.facilities.seatsFirstClass },
            trainParts.sumOf { it.facilities.seatsSecondClass },
            trainParts.map { it.facilities.features }.reduce { first, second -> first or second }
        )
    }

    data class TrainFacilities(
        val seatsFirstClass: Int = 0,
        val seatsSecondClass: Int = 0,
        val features: Int = 0
    ) {
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
    }

    data class TrainPart(
        val id: Int? = null,
        val facilities: TrainFacilities,
        val imageUrl: String? = null
    )
}
