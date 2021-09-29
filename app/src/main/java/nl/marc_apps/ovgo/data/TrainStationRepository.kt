package nl.marc_apps.ovgo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.db.TrainStationDao
import nl.marc_apps.ovgo.data.db.TrainStationEntity
import nl.marc_apps.ovgo.data.type_conversions.TrainStationConversions
import nl.marc_apps.ovgo.domain.TrainStation
import java.util.*
import java.util.concurrent.TimeUnit

class TrainStationRepository(
    private val trainStationDao: TrainStationDao,
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val preferences: DataStore<Preferences>
) {
    private var trainStationsCache: List<TrainStation>? = null

    suspend fun getTrainStations(): List<TrainStation> {
        return getTrainStationsFromCache()
            ?: getTrainStationsFromDatabase()
            ?: getTrainStationsFromApi()
            ?: emptyList()
    }

    suspend fun getTrainStationById(uicCode: String): TrainStation? {
        return getTrainStationsFromCache()?.find { it.uicCode == uicCode }
            ?: trainStationDao.getByUicCode(uicCode)?.asTrainStation()
            ?: getTrainStationsFromApi()?.find { it.uicCode == uicCode }
    }

    private fun getTrainStationsFromCache(): List<TrainStation>? {
        val cachedTrainStations = trainStationsCache
        return if (!cachedTrainStations.isNullOrEmpty()) cachedTrainStations else null
    }

    private suspend fun getTrainStationsFromDatabase(): List<TrainStation>? {
        if (isDatabaseOutdated()) {
            val stations = getTrainStationsFromApi()
            if (!stations.isNullOrEmpty()) {
                return stations
            }
        }

        val databaseTrainStations = trainStationDao.getAll()

        return if (!databaseTrainStations.isNullOrEmpty()) {
            databaseTrainStations.map {
                it.asTrainStation()
            }.also {
                trainStationsCache = it
            }
        } else null
    }

    private suspend fun getTrainStationsFromApi(): List<TrainStation>? {
        val trainStations = try {
            dutchRailwaysApi.getTrainStations().map {
                TrainStationConversions.convertApiToDomainModel(it)
            }
        } catch (error: Throwable) {
            return null
        }

        coroutineScope {
            launch {
                updateTrainStationDatabase(trainStations)
            }
        }

        return trainStations.also {
            trainStationsCache = it
        }
    }

    private suspend fun isDatabaseOutdated(): Boolean {
        val currentDate = Date().time
        val lastCacheDate = preferences.data.first()[trainStationCacheDatePreference] ?: currentDate
        val expirationTimeMs = TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)
        return currentDate - lastCacheDate > expirationTimeMs
    }

    private suspend fun updateTrainStationDatabase(updatedTrainStations: Collection<TrainStation>) {
        trainStationDao.insert(updatedTrainStations.map {
            TrainStationEntity.fromTrainStation(it)
        })

        preferences.edit {
            it[trainStationCacheDatePreference] = Date().time
        }
    }

    companion object {
        private val trainStationCacheDatePreference = longPreferencesKey("TRAIN_STATION_CACHE_DATE")
    }
}
