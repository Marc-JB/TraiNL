package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.type_conversions.ApiDepartureToDomainDeparture
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation
import retrofit2.HttpException

class DepartureRepository(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainInfoRepository: TrainInfoRepository,
    private val trainStationRepository: TrainStationRepository
) {
    private var cache: List<Departure> = emptyList()

    @Throws(KotlinNullPointerException::class, HttpException::class, Throwable::class)
    suspend fun getDepartures(station: TrainStation): List<Departure> {
        val departuresList = dutchRailwaysApi.getDeparturesForStation(station.uicCode)
        val stationsList = trainStationRepository.getTrainStations()
        val trainInfoList = trainInfoRepository.getTrainInfo(departuresList)

        val converter = ApiDepartureToDomainDeparture(stationsList, station, resolveTrainInfo = { journeyId ->
            trainInfoList.find { it.journeyId == journeyId.toInt() }
        })

        return departuresList.map {
            converter.convert(it)
        }.also {
            cache = it
        }
    }

    fun getDepartureById(id: String): Departure? {
        return cache.find { it.journeyId == id }
    }
}
