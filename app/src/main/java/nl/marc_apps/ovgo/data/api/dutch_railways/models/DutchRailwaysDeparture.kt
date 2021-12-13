package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.utils.serialization.DateSerializer
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
