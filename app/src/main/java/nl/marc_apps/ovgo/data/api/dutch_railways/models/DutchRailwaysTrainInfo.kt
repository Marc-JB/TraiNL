package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.domain.TrainInfo

@Serializable
data class DutchRailwaysTrainInfo (
    @SerialName("bron")
    val dataSource: DataSource? = null,
    @SerialName("ritnummer")
    val journeyNumber: Int,
    val station: String? = null,
    val type: String,
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
    fun asTrainInfo(): TrainInfo {
        val isQbuzzDMG = operator == "R-net" && actualTrainParts.none { it.type == "Flirt 2 TAG" }
        return TrainInfo(
            journeyNumber,
            actualTrainParts.map {
                if (isQbuzzDMG) {
                    val isLongTrainPart = "8" in it.type
                    TrainInfo.TrainPart(
                        it.id,
                        TrainInfo.TrainFacilities(
                            seatsFirstClass = 0,
                            seatsSecondClass = if (isLongTrainPart) 172 else 113,
                            hasToilet = false,
                            hasSilenceCompartment = false,
                            hasPowerSockets = true,
                            isWheelChairAccessible = true,
                            hasBicycleCompartment = true,
                            hasFreeWifi = true,
                            hasBistro = false,
                        ),
                        if (isLongTrainPart) {
                            "https://marc-jb.github.io/OVgo-api/gtw_qbuzz_28.png"
                        } else {
                            "https://marc-jb.github.io/OVgo-api/gtw_qbuzz_26.png"
                        }
                    )
                } else {
                    TrainInfo.TrainPart(
                        it.id,
                        TrainInfo.TrainFacilities(
                            (it.seats?.foldingChairsFirstClass ?: 0) + (it.seats?.seatsFirstClass
                                ?: 0),
                            (it.seats?.foldingChairsSecondClass ?: 0) + (it.seats?.seatsSecondClass
                                ?: 0),
                            TrainPart.Facility.TOILET in it.facilities,
                            TrainPart.Facility.SILENCE_COMPARTMENT in it.facilities,
                            TrainPart.Facility.POWER_SOCKETS in it.facilities,
                            TrainPart.Facility.WHEELCHAIR_ACCESSIBLE in it.facilities,
                            TrainPart.Facility.BICYCLE_COMPARTMENT in it.facilities,
                            TrainPart.Facility.WIFI in it.facilities,
                            TrainPart.Facility.BISTRO in it.facilities,
                        ),
                        if (it.type == "eurostar") {
                            "https://marc-jb.github.io/OVgo-api/eurostar_e320.png"
                        } else {
                            it.imageUrl
                        }
                    )
                }
            }
        )
    }

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
    ) : java.io.Serializable {

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
        ) : java.io.Serializable
    }

    enum class DataSource {
        DVS, KV6, OBIS, DAGPLAN, NMBS
    }

    @Serializable
    enum class Direction {
        @SerialName("LINKS")
        LEFT,
        @SerialName("RECHTS")
        RIGHT
    }
}
