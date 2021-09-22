package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.utils.DateSerializer
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

@Serializable
data class DutchRailwaysDeparture(
    val direction: String? = null,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val plannedDateTime: Date,
    @Serializable(with = DateSerializer::class)
    val actualDateTime: Date = plannedDateTime,
    val plannedTrack: String = "-",
    val actualTrack: String = plannedTrack,
    val product: DutchRailwaysProduct,
    val cancelled: Boolean = false,
    val routeStations: Set<DutchRailwaysRouteStation> = emptySet()
) : java.io.Serializable {
    val platformChanged: Boolean
        get() = actualTrack != plannedTrack

    val delayInSeconds: Long
        get() = TimeUnit.SECONDS.convert(actualDateTime.time - plannedDateTime.time, TimeUnit.MILLISECONDS)

    val delayInMinutesRounded
        get() = (delayInSeconds / 60.0).roundToInt()

    val isDelayed
        get() = delayInMinutesRounded > 0

    val departureTimeText
        get() = actualDateTime.format(timeStyle = DateFormat.SHORT)

    @Serializable
    data class DutchRailwaysProduct(
        val number: String,
        val longCategoryName: String,
        val operatorName: String
    ) : java.io.Serializable {
        val correctedOperatorName
            get() = when {
                operatorName.lowercase() != "r-net" -> operatorName
                longCategoryName.lowercase() == "sprinter" -> "R-net door NS"
                else -> "R-net door Qbuzz"
            }
    }

    @Serializable
    data class DutchRailwaysRouteStation(
        val uicCode: String,
        val mediumName: String
    ) : java.io.Serializable
}
