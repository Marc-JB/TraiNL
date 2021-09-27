package nl.marc_apps.ovgo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.db.TrainStationEntity
import nl.marc_apps.ovgo.data.db.TrainStationDao
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.ApiResult
import java.util.*
import java.util.concurrent.TimeUnit

class TrainStationRepository(
    private val trainStationDao: TrainStationDao,
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val preferences: DataStore<Preferences>
) {
    private var trainStationsCache: Set<TrainStation>? = null

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

    private fun getTrainStationsFromCache(): Set<TrainStation>? {
        val cachedTrainStations = trainStationsCache
        return if (!cachedTrainStations.isNullOrEmpty()) cachedTrainStations else null
    }

    private suspend fun getTrainStationsFromDatabase(): Set<TrainStation>? {
        if (isDatabaseOutdated()) {
            return getTrainStationsFromApi()
        }

        val databaseTrainStations = trainStationDao.getAll()

        return if (!databaseTrainStations.isNullOrEmpty()) {
            databaseTrainStations.map {
                it.asTrainStation()
            }.toSet().also {
                trainStationsCache = it
            }
        } else null
    }

    private suspend fun getTrainStationsFromApi(): Set<TrainStation>? {
        val trainStationResult = dutchRailwaysApi.getTrainStations()
        if (trainStationResult is ApiResult.Success) {
            val finalList = trainStationResult.body.map {
                it.asTrainStation()
            }

            coroutineScope {
                launch {
                    updateTrainStationDatabase(finalList)
                }
            }

            return finalList.toSet().also {
                trainStationsCache = it
            }
        } else if (trainStationResult is ApiResult.Failure) {
            Firebase.crashlytics.recordException(trainStationResult.apiError.error)
            trainStationResult.apiError.error.printStackTrace()
        }

        return null
    }

    private suspend fun isDatabaseOutdated(): Boolean {
        val currentDate = Date().time
        val lastCacheDate = preferences.data.first()[trainStationCacheDatePreference] ?: currentDate
        val expirationTimeMs = TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS)
        return currentDate - lastCacheDate > expirationTimeMs
    }

    private suspend fun updateTrainStationDatabase(updatedTrainStations: Collection<TrainStation>) {
        trainStationDao.insert(*updatedTrainStations.map {
            TrainStationEntity.fromTrainStation(it)
        }.toTypedArray())

        preferences.edit {
            it[trainStationCacheDatePreference] = Date().time
        }
    }

    companion object {
        private val trainStationCacheDatePreference = longPreferencesKey("TRAIN_STATION_CACHE_DATE")
    }
}
