package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DutchRailwaysTrainInfo (
    @SerialName("bron")
    val dataSource: String? = null,
    @SerialName("ritnummer")
    val journeyNumber: Int,
    val station: String? = null,
    val type: String? = null,
    @SerialName("vervoerder")
    val operator: String,
    @SerialName("spoor")
    val platform: String = "-",
    @SerialName("materieeldelen")
    val actualTrainParts: List<TrainPart> = emptyList(),
    @SerialName("geplandeMaterieeldelen")
    val plannedTrainParts: List<TrainPart> = emptyList(),
    @SerialName("ingekort")
    val shortened: Boolean = false,
    @SerialName("lengte")
    val actualAmountOfCoaches: Int,
    @SerialName("lengteInMeters")
    val lengthInMeters: Int? = null,
    @SerialName("geplandeLengte")
    val plannedAmountOfCoaches: Int? = null,
    @SerialName("rijrichting")
    val direction: Direction? = null
) {
    @Serializable
    data class TrainPart(
        @SerialName("materieelType")
        val trainType: String? = null,
        @SerialName("drukteSVGPath")
        val crowdednessSvgPath: String? = null,
        @SerialName("materieelnummer")
        val id: Int? = null,
        val type: String,
        @SerialName("faciliteiten")
        val facilities: Set<Facility> = emptySet(),
        @SerialName("eindbestemming")
        val destination: String? = null,
        @SerialName("afbeelding")
        val imageUrl: String? = null,
        @SerialName("zitplaatsen")
        val seats: TrainSeatsInfo? = null
    ) {
        @Serializable
        enum class Facility {
            @SerialName("TOILET")
            TOILET,
            @SerialName("STILTE")
            SILENCE_COMPARTMENT,
            @SerialName("STROOM")
            POWER_SOCKETS,
            @SerialName("TOEGANKELIJK")
            WHEELCHAIR_ACCESSIBLE,
            @SerialName("FIETS")
            BICYCLE_COMPARTMENT,
            @SerialName("WIFI")
            WIFI,
            @SerialName("BISTRO")
            BISTRO
        }

        @Serializable
        data class TrainSeatsInfo(
            @SerialName("zitplaatsEersteKlas")
            val seatsFirstClass: Int = 0,
            @SerialName("zitplaatsTweedeKlas")
            val seatsSecondClass: Int = 0,
            @SerialName("klapstoelEersteKlas")
            val foldingChairsFirstClass: Int = 0,
            @SerialName("klapstoelTweedeKlas")
            val foldingChairsSecondClass: Int = 0,
        )
    }

    @Serializable
    enum class Direction {
        @SerialName("LINKS")
        LEFT,
        @SerialName("RECHTS")
        RIGHT
    }
}
