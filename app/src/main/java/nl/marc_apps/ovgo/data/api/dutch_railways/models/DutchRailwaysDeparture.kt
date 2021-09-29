package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.DateSerializer
import java.util.*

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
) {
    fun asDeparture(
        resolveUicCode: (String) -> TrainStation?,
        resolveStationName: (String) -> TrainStation?,
        resolveTrainInfo: (String) -> TrainInfo?
    ): Departure {
        val resolvedDirection = direction?.let(resolveStationName)
            ?: routeStations.lastOrNull()?.uicCode?.let(resolveUicCode)
            ?: routeStations.lastOrNull()?.mediumName?.let(resolveStationName)

        val operator = when {
            product.longCategoryName == "Thalys" || product.longCategoryName == "Eurostar" -> product.longCategoryName
            product.operatorName.lowercase() != "r-net" -> product.operatorName
            product.longCategoryName.lowercase() == "sprinter" -> "R-net door NS"
            else -> "R-net door Qbuzz"
        }

        return Departure(
            product.number,
            resolvedDirection,
            plannedDateTime,
            actualDateTime,
            plannedTrack,
            actualTrack,
            resolveTrainInfo(product.number),
            operator,
            product.longCategoryName,
            cancelled,
            routeStations.mapNotNull {
                resolveUicCode(it.uicCode) ?: resolveStationName(it.mediumName)
            }
        )
    }

    @Serializable
    data class DutchRailwaysProduct(
        val number: String,
        val longCategoryName: String,
        val operatorName: String
    )

    @Serializable
    data class DutchRailwaysRouteStation(
        val uicCode: String,
        val mediumName: String
    )
}
