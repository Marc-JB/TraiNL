package nl.marc_apps.ovgo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStation
import nl.marc_apps.ovgo.data.db.TrainStationEntity
import nl.marc_apps.ovgo.data.db.TrainStationDao
import nl.marc_apps.ovgo.domain.TrainStation
import java.util.*
import java.util.concurrent.TimeUnit

class TrainStationRepository(
    private val trainStationDao: TrainStationDao,
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val preferences: DataStore<Preferences>
) {
    private var trainStationsCache: Set<TrainStation>? = null

    private fun getTrainStationsFromCache(): Set<TrainStation>? {
        val cachedTrainStations = trainStationsCache
        if (!cachedTrainStations.isNullOrEmpty()) {
            return cachedTrainStations
        }

        return null
    }

    private suspend fun getTrainStationsFromDatabase(): Set<TrainStation>? {
        val currentDate = Date().time
        val lastCacheDate = preferences.data.first()[trainStationCacheDatePreference] ?: currentDate
        if (currentDate - lastCacheDate > TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)) {
            return getTrainStationsFromApi()
        }

        val databaseTrainStations = trainStationDao.getAll()

        if (!databaseTrainStations.isNullOrEmpty()) {
            return databaseTrainStations.map {
                it.asTrainStation()
            }.toSet().also {
                trainStationsCache = it
            }
        }

        return null
    }

    private suspend fun getTrainStationsFromApi(): Set<TrainStation>? {
        val apiTrainStations = dutchRailwaysApi.getTrainStations().bodyOrNull ?: return null

        val finalList = apiTrainStations.map {
            it.asTrainStation()
        }

        coroutineScope {
            launch {
                trainStationDao.insert(*finalList.map {
                    TrainStationEntity.fromTrainStation(it)
                }.toTypedArray())

                preferences.edit {
                    it[trainStationCacheDatePreference] = Date().time
                }
            }
        }

        return finalList.toSet().also {
            trainStationsCache = it
        }
    }

    suspend fun getTrainStations(): Set<TrainStation> {
        return getTrainStationsFromCache()
            ?: getTrainStationsFromDatabase()
            ?: getTrainStationsFromApi()
            ?: emptySet()
    }

    suspend fun getTrainStationById(uicCode: String): TrainStation? {
        return getTrainStationsFromCache()?.find { it.uicCode == uicCode }
            ?: trainStationDao.getByUicCode(uicCode)?.asTrainStation()
            ?: getTrainStationsFromApi()?.find { it.uicCode == uicCode }
    }

    companion object {
        private val trainStationCacheDatePreference = longPreferencesKey("TRAIN_STATION_CACHE_DATE")
    }
}