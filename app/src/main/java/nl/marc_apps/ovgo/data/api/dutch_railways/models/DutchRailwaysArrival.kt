package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.data.api.dutch_railways.utils.InstantIso8601Serializer

@Serializable
data class DutchRailwaysArrival(
    val origin: String? = null,
    val name: String,
    @Serializable(with = InstantIso8601Serializer::class)
    val plannedDateTime: Instant,
    @Serializable(with = InstantIso8601Serializer::class)
    val actualDateTime: Instant = plannedDateTime,
    val plannedTrack: String = "-",
    val actualTrack: String = plannedTrack,
    val product: DutchRailwaysProduct,
    val cancelled: Boolean = false
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
