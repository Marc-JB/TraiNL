package nl.marc_apps.ovgo.data

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toSet
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStop
import nl.marc_apps.ovgo.domain.TrainStation
import retrofit2.HttpException

class JourneyDetailsRepository(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainStationRepository: TrainStationRepository
) {
    @Throws(KotlinNullPointerException::class, HttpException::class, Throwable::class)
    suspend fun getStops(journeyId: String): Set<TrainStation> {
        val departuresList = dutchRailwaysApi.getJourneyDetails(journeyId)

        val stationIds = departuresList.filterNot {
            it.status == DutchRailwaysStop.Status.PASSING || isCancelled(it)
        }.map {
            it.stop.uicCode
        }

        return flow {
            stationIds.forEach {
                emit(it)
            }
        }.mapNotNull {
            trainStationRepository.getTrainStationById(it)
        }.toSet()
    }

    private fun isCancelled(stop: DutchRailwaysStop): Boolean {
        val arrivalsCancelled = stop.arrivals.all { it.cancelled }
        val departuresCancelled = stop.departures.all { it.cancelled }
        return (arrivalsCancelled && departuresCancelled)
                || (arrivalsCancelled && stop.departures.isEmpty())
                || (departuresCancelled && stop.arrivals.isEmpty())
    }
}
