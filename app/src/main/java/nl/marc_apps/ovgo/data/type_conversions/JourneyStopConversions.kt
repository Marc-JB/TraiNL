package nl.marc_apps.ovgo.data.type_conversions

import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStop
import nl.marc_apps.ovgo.domain.JourneyStop
import kotlin.math.roundToInt

object JourneyStopConversions {
    suspend fun convert(stop: DutchRailwaysStop, trainStationRepository: TrainStationRepository): JourneyStop? {
        if (stop.nextStopId.size > 1 || stop.nextStopId.size > 1) {
            return null
        }

        val trainStation = trainStationRepository.getTrainStationById(stop.stop.uicCode) ?: return null

        val punctuality = stop.arrivals.mapNotNull {
            it.punctuality
        } + stop.departures.mapNotNull {
            it.punctuality
        }

        val firstDeparture = stop.departures.minByOrNull { it.actualTime }
        val lastArrival = stop.arrivals.maxByOrNull { it.actualTime }

        return JourneyStop(
            stop.id,
            trainStation,
            lastArrival?.plannedTime,
            lastArrival?.actualTime,
            firstDeparture?.plannedTime,
            firstDeparture?.actualTime,
            (firstDeparture ?: lastArrival)?.plannedTrack ?: return null,
            (firstDeparture ?: lastArrival)?.actualTrack ?: return null,
            isCancelled(stop),
            if (punctuality.isEmpty()) null else punctuality.average().roundToInt()
        )
    }

    private fun isCancelled(stop: DutchRailwaysStop): Boolean {
        val arrivalsCancelled = stop.arrivals.all { it.cancelled }
        val departuresCancelled = stop.departures.all { it.cancelled }
        return (arrivalsCancelled && departuresCancelled)
                || (arrivalsCancelled && stop.departures.isEmpty())
                || (departuresCancelled && stop.arrivals.isEmpty())
    }
}
