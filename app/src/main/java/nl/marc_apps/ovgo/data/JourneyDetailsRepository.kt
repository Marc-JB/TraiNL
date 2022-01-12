package nl.marc_apps.ovgo.data

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toSet
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStop
import nl.marc_apps.ovgo.data.type_conversions.JourneyStopConversions
import nl.marc_apps.ovgo.domain.JourneyStop
import retrofit2.HttpException

class JourneyDetailsRepository(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainStationRepository: TrainStationRepository
) {
    @Throws(KotlinNullPointerException::class, HttpException::class, Throwable::class)
    suspend fun getStops(journeyId: String): Set<JourneyStop> {
        val departuresList = dutchRailwaysApi.getJourneyDetails(journeyId)

        val stops = departuresList.filterNot {
            it.status == DutchRailwaysStop.Status.PASSING
        }

        return flow {
            stops.forEach {
                emit(it)
            }
        }.mapNotNull {
            JourneyStopConversions.convert(it, trainStationRepository)
        }.toSet()
    }
}
