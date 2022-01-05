package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.utils.serialization.DateSerializer
import java.util.*

@Serializable
data class DutchRailwaysStop(
    val id: String,
    val stop: StationDetails,
    val previousStopId: Set<String> = emptySet(),
    val nextStopId: Set<String> = emptySet(),
    val destination: String? = null,
    val status: Status,
    val arrivals: Set<DepartureOrArrival> = emptySet(),
    val departures: Set<DepartureOrArrival> = emptySet()
) {
    @Serializable
    data class StationDetails(
        val name: String,
        val countryCode: String,
        val uicCode: String
    )

    @Serializable
    data class DepartureOrArrival(
        val product: Product,
        val origin: StationDetails,
        val destination: StationDetails,
        @Serializable(with = DateSerializer::class)
        val plannedTime: Date,
        @Serializable(with = DateSerializer::class)
        val actualTime: Date,
        val delayInSeconds: Int,
        val plannedTrack: String,
        val actualTrack: String,
        val cancelled: Boolean,
        val punctuality: Double? = null,
        val crowdForecast: CrowdForecast
    ) {
        @Serializable
        data class Product(
            @SerialName("number")
            val journeyId: String
        )

        enum class CrowdForecast {
            LOW, MEDIUM, HIGH, UNKNOWN
        }
    }

    enum class Status {
        ORIGIN, STOP, PASSING, DESTINATION
    }
}
