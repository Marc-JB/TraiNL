package nl.marc_apps.ovgo.data.api.dutch_railways.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.marc_apps.ovgo.data.api.dutch_railways.utils.InstantIso8601Serializer

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
        val origin: StationDetails? = null,
        val destination: StationDetails? = null,
        @Serializable(with = InstantIso8601Serializer::class)
        val plannedTime: Instant,
        @Serializable(with = InstantIso8601Serializer::class)
        val actualTime: Instant = plannedTime,
        val delayInSeconds: Int = 0,
        val plannedTrack: String,
        val actualTrack: String = plannedTrack,
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
