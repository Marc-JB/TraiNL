package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.DateSerializer
import java.util.*

@Serializable
data class DutchRailwaysStation(
    @SerialName("UICCode")
    val uicCode: String,
    val stationType: StationType,
    @SerialName("EVACode")
    val evaCode: String,
    @SerialName("code")
    val dutchRailwaysCode: String,
    @SerialName("sporen")
    val platforms: Set<Platform>,
    @SerialName("synoniemen")
    val customNames: Set<String>,
    @SerialName("heeftFaciliteiten")
    val hasFacilities: Boolean,
    @SerialName("heeftVertrektijden")
    val hasDepartureTimesBoard: Boolean,
    @SerialName("heeftReisassistentie")
    val hasTravelAssistance: Boolean,
    @SerialName("namen")
    val names: Names,
    @SerialName("land")
    val country: Country,
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lng")
    val longitude: Double,
    val radius: Int,
    @SerialName("naderenRadius")
    val approachRadius: Int,
    @SerialName("ingangsDatum")
    @Serializable(with = DateSerializer::class)
    val sinceDate: Date
) : java.io.Serializable {
    fun asTrainStation(): TrainStation {
        return TrainStation(
            uicCode,
            names.long,
            customNames,
            setOf(dutchRailwaysCode, names.middle, names.short),
            hasDepartureTimesBoard,
            hasTravelAssistance,
            TrainStation.Country.valueOf(country.name)
        )
    }

    @Serializable
    data class Names(
        @SerialName("lang")
        val long: String,
        @SerialName("middel")
        val middle: String,
        @SerialName("kort")
        val short: String
    ) : java.io.Serializable

    @Serializable
    data class Platform(
        @SerialName("spoorNummer")
        val name: String
    ) : java.io.Serializable

    @Serializable
    enum class StationType {
        @SerialName("FACULTATIEF_STATION")
        FACULTATIVE_STATION,
        @SerialName("INTERCITY_STATION")
        INTERCITY_STATION,
        @SerialName("KNOOPPUNT_INTERCITY_STATION")
        JUNCTION_INTERCITY_STATION,
        @SerialName("KNOOPPUNT_SNELTREIN_STATION")
        JUNCTION_EXPRESS_TRAIN_STATION,
        @SerialName("KNOOPPUNT_STOPTREIN_STATION")
        JUNCTION_STOP_TRAIN_STATION,
        @SerialName("MEGA_STATION")
        MEGA_STATION,
        @SerialName("SNELTREIN_STATION")
        EXPRESS_TRAIN_STATION,
        @SerialName("STOPTREIN_STATION")
        STOP_TRAIN_STATION
    }

    @Serializable
    enum class Country {
        @SerialName("A")
        AUSTRIA,
        @SerialName("B")
        BELGIUM,
        @SerialName("CH")
        SWITZERLAND,
        @SerialName("D")
        GERMANY,
        @SerialName("F")
        FRANCE,
        @SerialName("GB")
        GREAT_BRITAIN,
        @SerialName("NL")
        THE_NETHERLANDS,
    }
}
