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

        val hasArrivals = stop.arrivals.none {
            it.cancelled
                    || it.origin == null
                    || it.origin.uicCode == stop.stop.uicCode
                    || it.destination == null
        }
        val hasDepartures = stop.departures.none {
            it.cancelled
                    || it.origin == null
                    || it.destination == null
                    || it.destination.uicCode == stop.stop.uicCode
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
            if (punctuality.isEmpty()) null else punctuality.average().roundToInt()
        )
    }
}
