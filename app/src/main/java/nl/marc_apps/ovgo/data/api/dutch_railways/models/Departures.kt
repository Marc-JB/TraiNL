package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable

@Serializable
data class Departures(
    val source: String? = null,
    val departures: List<DutchRailwaysDeparture?>
)
