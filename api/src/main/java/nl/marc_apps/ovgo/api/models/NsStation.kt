package nl.marc_apps.ovgo.api.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import nl.marc_apps.ovgo.domain.models.Coordinates
import nl.marc_apps.ovgo.domain.models.Country
import nl.marc_apps.ovgo.domain.models.TrainStation
import nl.marc_apps.ovgo.domain.models.TrainStationFacilities

@Serializable
data class NsStation (
    val sporen: List<Sporen>,
    val synoniemen: List<String>,
    val heeftFaciliteiten: Boolean,
    val heeftVertrektijden: Boolean,
    val heeftReisassistentie: Boolean,
    val code: String,
    val namen: Namen,
    val stationType: StationType,
    val land: Land,

    @SerialName("UICCode")
    val uicCode: String,

    val lat: Double,
    val lng: Double,
    val radius: Long,
    val naderenRadius: Long,

    @SerialName("EVACode")
    val evaCode: String,

    val ingangsDatum: String
)

fun NsStation.asStation() = TrainStation(
    uicCode.toInt(),
    code,
    namen.lang,
    Country(land.code, land.code, land.code),
    TrainStationFacilities(
        heeftReisassistentie,
        heeftVertrektijden
    ),
    Coordinates(lat.toFloat(), lng.toFloat()),
    synoniemen.toTypedArray(),
    sporen.map { it.spoorNummer }.toTypedArray()
)

@Serializable(with = Land.Companion::class)
enum class Land(val value: String, val code: String = value) {
    AT("A", "AT"),
    BE("B", "BE"),
    CH("CH"),
    DE("D", "DE"),
    FR("F", "FR"),
    GB("GB"),
    NL("NL");

    companion object : KSerializer<Land> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("quicktype.Land", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Land = when (val value = decoder.decodeString()) {
            "A"  -> AT
            "B"  -> BE
            "CH" -> CH
            "D"  -> DE
            "F"  -> FR
            "GB" -> GB
            "NL" -> NL
            else -> throw IllegalArgumentException("Land could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: Land) {
            return encoder.encodeString(value.code)
        }
    }
}

@Serializable
data class Namen (
    val lang: String,
    val kort: String,
    val middel: String
)

@Serializable
data class Sporen (
    val spoorNummer: String
)

@Serializable(with = StationType.Companion::class)
enum class StationType(val value: String) {
    FacultatiefStation("FACULTATIEF_STATION"),
    IntercityStation("INTERCITY_STATION"),
    KnooppuntIntercityStation("KNOOPPUNT_INTERCITY_STATION"),
    KnooppuntSneltreinStation("KNOOPPUNT_SNELTREIN_STATION"),
    KnooppuntStoptreinStation("KNOOPPUNT_STOPTREIN_STATION"),
    MegaStation("MEGA_STATION"),
    SneltreinStation("SNELTREIN_STATION"),
    StoptreinStation("STOPTREIN_STATION");

    companion object : KSerializer<StationType> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("quicktype.StationType", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): StationType = when (val value = decoder.decodeString()) {
            "FACULTATIEF_STATION"         -> FacultatiefStation
            "INTERCITY_STATION"           -> IntercityStation
            "KNOOPPUNT_INTERCITY_STATION" -> KnooppuntIntercityStation
            "KNOOPPUNT_SNELTREIN_STATION" -> KnooppuntSneltreinStation
            "KNOOPPUNT_STOPTREIN_STATION" -> KnooppuntStoptreinStation
            "MEGA_STATION"                -> MegaStation
            "SNELTREIN_STATION"           -> SneltreinStation
            "STOPTREIN_STATION"           -> StoptreinStation
            else                          -> throw IllegalArgumentException("StationType could not parse: $value")
        }

        override fun serialize(encoder: Encoder, value: StationType) {
            return encoder.encodeString(value.value)
        }
    }
}
