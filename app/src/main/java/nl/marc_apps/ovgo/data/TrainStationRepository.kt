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
import nl.marc_apps.ovgo.utils.getOrNull
import java.util.concurrent.TimeUnit

class TrainStationRepository(
    private val trainStationDao: TrainStationDao,
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val preferences: DataStore<Preferences>
) {
    private var trainStationsCache: List<TrainStation>? = null

    private fun updateCache(trainStations: List<TrainStation>) {
        trainStationsCache = trainStations
    }

    suspend fun getTrainStations(): List<TrainStation> {
        val trainStationsFromCache = getTrainStationsFromCache()
        if (trainStationsFromCache != null) {
            return trainStationsFromCache
        }

        if (!isDatabaseOutdatedOrEmpty()) {
            return getTrainStationsFromDatabase() ?: emptyList()
        }

        val apiTrainStations = getTrainStationsFromApi()
        return if (apiTrainStations.isNullOrEmpty()) {
            if (hasCacheInDatabase()){
                getTrainStationsFromDatabase()?.also(::updateCache) ?: emptyList()
            } else emptyList()
        } else {
            updateTrainStationDatabaseInBackground(apiTrainStations)
            apiTrainStations.also(::updateCache)
        }
    }

    suspend fun getTrainStationById(uicCode: String): TrainStation? {
        return getTrainStationsFromCache()?.find { it.uicCode == uicCode }
            ?: trainStationDao.getByUicCode(uicCode)?.asTrainStation()
            ?: getTrainStationsFromApi()?.find { it.uicCode == uicCode }
    }

    private fun getTrainStationsFromCache(): List<TrainStation>? {
        val cachedTrainStations = trainStationsCache
        return if (cachedTrainStations.isNullOrEmpty()) null else cachedTrainStations
    }

    private suspend fun getTrainStationsFromDatabase(): List<TrainStation>? {
        val databaseTrainStations = trainStationDao.getAll()

        return if (databaseTrainStations.isNullOrEmpty()) {
            null
        } else {
            databaseTrainStations.map {
                it.asTrainStation()
            }
        }
    }

    private suspend fun hasCacheInDatabase(): Boolean {
        return trainStationDao.getSize() >= 0
    }

    private suspend fun updateTrainStationDatabaseInBackground(updatedTrainStations: Collection<TrainStation>) {
        coroutineScope {
            launch {
                updateTrainStationDatabase(updatedTrainStations)
            }
        }
    }

    private suspend fun updateTrainStationDatabase(updatedTrainStations: Collection<TrainStation>) {
        trainStationDao.deleteAll()

        trainStationDao.insert(updatedTrainStations.map {
            TrainStationEntity.fromTrainStation(it)
        })

        preferences.edit {
            it[trainStationCacheDatePreference] = System.currentTimeMillis()
        }
    }

    private suspend fun isDatabaseOutdatedOrEmpty(): Boolean {
        val lastCacheDate = preferences.getOrNull(trainStationCacheDatePreference) ?: return true
        val expirationTimeMs = TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)
        val currentDate = System.currentTimeMillis()
        return currentDate - lastCacheDate > expirationTimeMs
    }

    private suspend fun getTrainStationsFromApi(): List<TrainStation>? {
        val trainStations = try {
            dutchRailwaysApi.getTrainStations().map {
                TrainStationConversions.convertApiToDomainModel(it)
            }
        } catch (error: Throwable) {
            return null
        }

        return trainStations
    }

    companion object {
        private val trainStationCacheDatePreference = longPreferencesKey("TRAIN_STATION_CACHE_DATE")
    }
}
