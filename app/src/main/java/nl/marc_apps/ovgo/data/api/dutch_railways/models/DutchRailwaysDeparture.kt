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
    // TODO: Remove this from model
    fun asDeparture(
        resolveUicCode: (String) -> TrainStation?,
        resolveStationName: (String) -> TrainStation?,
        resolveTrainInfo: (String) -> TrainInfo?
    ): Departure {
        val resolvedDirection = direction?.let(resolveStationName)
            ?: routeStations.lastOrNull()?.uicCode?.let(resolveUicCode)
            ?: routeStations.lastOrNull()?.mediumName?.let(resolveStationName)

        val operator = when {
            product.longCategoryName == TRAIN_SERVICE_THALYS -> TRAIN_SERVICE_THALYS
            product.longCategoryName == TRAIN_SERVICE_EUROSTAR -> TRAIN_SERVICE_EUROSTAR
            !product.operatorName.equals(OPERATOR_RNET, ignoreCase = true) -> product.operatorName
            product.longCategoryName.equals(TRAIN_CATEGORY_SPRINTER, ignoreCase = true) -> OPERATOR_RNET_BY_NS
            else -> OPERATOR_RNET_BY_QBUZZ
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

    companion object {
        private const val TRAIN_SERVICE_THALYS = "Thalys"

        private const val TRAIN_SERVICE_EUROSTAR = "Eurostar"

        private const val OPERATOR_RNET = "R-net"

        private const val TRAIN_CATEGORY_SPRINTER = "Sprinter"

        // TODO: Migrate this constant to a string resource
        private const val OPERATOR_RNET_BY_QBUZZ = "R-net door Qbuzz"

        // TODO: Migrate this constant to a string resource
        private const val OPERATOR_RNET_BY_NS = "R-net door NS"
    }
}
