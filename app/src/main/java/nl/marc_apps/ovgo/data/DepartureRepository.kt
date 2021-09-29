package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation

class DepartureRepository(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainInfoRepository: TrainInfoRepository,
    private val trainStationRepository: TrainStationRepository
) {
    suspend fun getDepartures(station: TrainStation): List<Departure> {
        val departuresList = try {
            dutchRailwaysApi.getDeparturesForStation(station.uicCode)
        } catch (error: Throwable) {
            return emptyList()
        }
        val stationsList = trainStationRepository.getTrainStations()
        val trainInfoList = trainInfoRepository.getTrainInfo(departuresList.map { it.product.number.toInt() }.toSet())

        return departuresList.map {
            it.asDeparture(
                resolveUicCode = { uicCode ->
                    stationsList.find { it.uicCode == uicCode  }
                },
                resolveStationName = { stationName ->
                    stationsList.find {
                        it.name == stationName
                                || stationName in station.alternativeNames
                                || stationName in station.alternativeSearches
                    }
                },
                resolveTrainInfo = { journeyId ->
                    trainInfoList.find { it.journeyId == journeyId.toInt() }
                }
            )
        }
    }
}
