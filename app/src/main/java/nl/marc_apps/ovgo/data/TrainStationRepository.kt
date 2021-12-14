package nl.marc_apps.ovgo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.db.TrainStationDao
import nl.marc_apps.ovgo.data.db.TrainStationEntity
import nl.marc_apps.ovgo.data.type_conversions.TrainStationConversions
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.getOrNull
import nl.marc_apps.ovgo.utils.println
import java.util.concurrent.TimeUnit

class TrainStationRepository(
    private val trainStationDao: TrainStationDao,
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val preferences: DataStore<Preferences>
) {
    private var trainStationsCache: List<TrainStation>? = null

    private fun updateMemoryCache(trainStations: List<TrainStation>) {
        trainStationsCache = trainStations
    }

    private fun logSource(source: String, remark: String? = null) {
        println(TAG, "Train stations fetched from $source." + if (remark == null) "" else " $remark.")
    }

    suspend fun getTrainStations(): List<TrainStation> {
        val trainStationsFromCache = getTrainStationsFromCache()
        if (trainStationsFromCache != null) {
            logSource("MEMORY_CACHE")
            return trainStationsFromCache
        }

        if (!isDatabaseOutdatedOrEmpty()) {
            logSource("DB")
            return getTrainStationsFromDatabase()?.also(::updateMemoryCache) ?: emptyList()
        }

        val apiTrainStations = getTrainStationsFromApi()
        return if (apiTrainStations.isNullOrEmpty()) {
            if (hasCacheInDatabase()){
                logSource("DB", "API call has failed")
                getTrainStationsFromDatabase()?.also(::updateMemoryCache) ?: emptyList()
            } else emptyList()
        } else {
            logSource("API")
            updateTrainStationDatabaseInBackground(apiTrainStations)
            apiTrainStations.also(::updateMemoryCache)
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
            it[PreferenceKeys.trainStationCacheDatePreference] = System.currentTimeMillis()
        }
    }

    private suspend fun isDatabaseOutdatedOrEmpty(): Boolean {
        val lastCacheDate = preferences.getOrNull(PreferenceKeys.trainStationCacheDatePreference) ?: return true
        val currentDate = System.currentTimeMillis()
        return currentDate - lastCacheDate > databaseCacheExpirationTimeMs
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
        private const val TAG = "TRAIN_STATION_REPO"

        private val databaseCacheExpirationTimeMs = TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)
    }
}
