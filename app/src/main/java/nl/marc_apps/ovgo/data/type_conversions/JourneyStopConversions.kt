package nl.marc_apps.ovgo.data.type_conversions

import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStop
import nl.marc_apps.ovgo.domain.JourneyStop
import kotlin.math.roundToInt

object JourneyStopConversions {
    private fun getTotalPunctuality(stop: DutchRailwaysStop): Int? {
        val list = stop.arrivals.mapNotNull {
            it.punctuality
        } + stop.departures.mapNotNull {
            it.punctuality
        }

        return if (list.isEmpty()) null else list.average().roundToInt()
    }

    private fun isDepartureOrArrivalCancelled(
        departureOrArrival: DutchRailwaysStop.DepartureOrArrival,
        isDeparture: Boolean,
        currentStationUicCode: String
    ): Boolean {
        return departureOrArrival.cancelled ||
                departureOrArrival.origin == null ||
                departureOrArrival.destination == null ||
                (if(isDeparture) {
                    departureOrArrival.destination
                } else {
                    departureOrArrival.origin
                }).uicCode == currentStationUicCode
    }

    suspend fun convert(stop: DutchRailwaysStop, trainStationRepository: TrainStationRepository): JourneyStop? {
        if (stop.previousStopId.size > 1 || stop.nextStopId.size > 1) {
            return null
        }

        val trainStation = trainStationRepository.getTrainStationById(stop.stop.uicCode) ?: return null

        val hasArrivals = stop.arrivals.none {
            isDepartureOrArrivalCancelled(it, false, stop.stop.uicCode)
        }

        val hasDepartures = stop.departures.none {
            isDepartureOrArrivalCancelled(it, true, stop.stop.uicCode)
        }

        val isCancelled = (!hasArrivals && !hasDepartures)
                || (!hasArrivals && stop.departures.isEmpty())
                || (!hasDepartures && stop.arrivals.isEmpty())

        val firstDeparture = stop.departures.minByOrNull { it.actualTime }
        val lastArrival = stop.arrivals.maxByOrNull { it.actualTime }

        val hideArrival = isCancelled || (!hasArrivals && hasDepartures)
        val hideDeparture = isCancelled || (!hasDepartures && hasArrivals)

        return JourneyStop(
            stop.id,
            trainStation,
            if (hideArrival) null else lastArrival?.plannedTime,
            if (hideArrival) null else lastArrival?.actualTime,
            if (hideDeparture) null else firstDeparture?.plannedTime,
            if (hideDeparture) null else firstDeparture?.actualTime,
            (firstDeparture ?: lastArrival)?.plannedTrack ?: return null,
            (firstDeparture ?: lastArrival)?.actualTrack ?: return null,
            isCancelled,
            getTotalPunctuality(stop)
        )
    }
}
