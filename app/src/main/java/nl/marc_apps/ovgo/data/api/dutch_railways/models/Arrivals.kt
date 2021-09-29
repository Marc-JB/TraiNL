package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.Serializable

@Serializable
data class Arrivals(
    val source: String? = null,
    val arrivals: List<DutchRailwaysArrival?>
)
