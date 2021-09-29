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
    // TODO: Remove this from model
    fun asTrainInfo(): TrainInfo {
        val isQbuzzDMG = operator == OPERATOR_NAME_RNET && actualTrainParts.none {
            it.type == TRAIN_TYPE_RNET_BY_NS
        }

        return TrainInfo(
            journeyNumber,
            actualTrainParts.map {
                it.asTrainInfoPart(isQbuzzDMG)
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
    ) {
        // TODO: Remove this from model
        fun asTrainInfoPart(isQbuzzDMG: Boolean): TrainInfo.TrainPart {
            return if (isQbuzzDMG) {
                val isLongTrainPart = "8" in type
                TrainInfo.TrainPart(
                    id,
                    TrainInfo.TrainFacilities(
                        seatsFirstClass = NO_SEATS,
                        seatsSecondClass = if (isLongTrainPart) TRAIN_SEAT_COUNT_QBUZZ_GTW_8 else TRAIN_SEAT_COUNT_QBUZZ_GTW_6,
                        hasToilet = false,
                        hasSilenceCompartment = false,
                        hasPowerSockets = true,
                        isWheelChairAccessible = true,
                        hasBicycleCompartment = true,
                        hasFreeWifi = true,
                        hasBistro = false,
                    ),
                    if (isLongTrainPart) {
                        TRAIN_IMAGE_URL_QBUZZ_GTW_8
                    } else {
                        TRAIN_IMAGE_URL_QBUZZ_GTW_6
                    }
                )
            } else {
                val seatCountFirstClass = (seats?.foldingChairsFirstClass ?: NO_SEATS) + (seats?.seatsFirstClass ?: NO_SEATS)
                val seatCountSecondClass = (seats?.foldingChairsSecondClass ?: NO_SEATS) + (seats?.seatsSecondClass ?: NO_SEATS)
                TrainInfo.TrainPart(
                    id,
                    TrainInfo.TrainFacilities(
                        seatCountFirstClass,
                        seatCountSecondClass,
                        Facility.TOILET in facilities,
                        Facility.SILENCE_COMPARTMENT in facilities,
                        Facility.POWER_SOCKETS in facilities,
                        Facility.WHEELCHAIR_ACCESSIBLE in facilities,
                        Facility.BICYCLE_COMPARTMENT in facilities,
                        Facility.WIFI in facilities,
                        Facility.BISTRO in facilities,
                    ),
                    if (type == TRAIN_TYPE_EUROSTAR.lowercase()) {
                        TRAIN_IMAGE_URL_EUROSTAR
                    } else {
                        imageUrl
                    }
                )
            }
        }

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

    companion object {
        private const val TRAIN_IMAGE_URL_QBUZZ_GTW_6 = "https://marc-jb.github.io/OVgo-api/gtw_qbuzz_26.png"

        private const val TRAIN_IMAGE_URL_QBUZZ_GTW_8 = "https://marc-jb.github.io/OVgo-api/gtw_qbuzz_28.png"

        private const val TRAIN_IMAGE_URL_EUROSTAR = "https://marc-jb.github.io/OVgo-api/eurostar_e320.png"

        private const val TRAIN_SEAT_COUNT_QBUZZ_GTW_6 = 113

        private const val TRAIN_SEAT_COUNT_QBUZZ_GTW_8 = 172

        private const val NO_SEATS = 0

        private const val OPERATOR_NAME_RNET = "R-net"

        private const val TRAIN_TYPE_RNET_BY_NS = "Flirt 2 TAG"

        private const val TRAIN_TYPE_EUROSTAR = "Eurostar"
    }
}
