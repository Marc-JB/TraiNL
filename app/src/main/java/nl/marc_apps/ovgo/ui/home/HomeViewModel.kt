package nl.marc_apps.ovgo.ui.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.TrainInfoRepository
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionType
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.ApiResult
import java.util.*

class HomeViewModel(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainStationRepository: TrainStationRepository,
    private val trainInfoRepository: TrainInfoRepository,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private val mutableCurrentStation = MutableLiveData<TrainStation>()

    val currentStation: LiveData<TrainStation>
        get() = mutableCurrentStation

    private val mutableDepartures = MutableLiveData<Set<Pair<DutchRailwaysDeparture, TrainInfo?>>?>()

    val departures: LiveData<Set<Pair<DutchRailwaysDeparture, TrainInfo?>>?>
        get() = mutableDepartures

    private val mutableDisruptions = MutableLiveData<Set<DutchRailwaysDisruption>?>()

    val disruptions: LiveData<Set<DutchRailwaysDisruption>?>
        get() = mutableDisruptions

    private val mutableMaintenanceList = MutableLiveData<Set<DutchRailwaysDisruption>?>()

    val maintenanceList: LiveData<Set<DutchRailwaysDisruption>?>
        get() = mutableMaintenanceList

    fun loadDeparturesForLastKnownStation() {
        viewModelScope.launch {
            val lastKnownStationId = preferences.data.first()[lastTrainStation]
            if (lastKnownStationId != null) {
                val station = trainStationRepository.getTrainStationById(lastKnownStationId)
                if (station != null) {
                    loadDepartures(station)
                    return@launch
                }
            }

            val station = trainStationRepository.getTrainStations().firstOrNull {
                it.name == DEFAULT_STATION_NAME
            }
            if (station != null) {
                loadDepartures(station)
            }
        }
    }

    fun loadDepartures(station: TrainStation, allowReload: Boolean = false) {
        if (!allowReload && !mutableDepartures.value.isNullOrEmpty()) {
            return
        }

        mutableCurrentStation.postValue(station)
        viewModelScope.launch {
            preferences.edit {
                it[lastTrainStation] = station.uicCode
            }
        }
        mutableDepartures.postValue(null)

        viewModelScope.launch {
            val departuresResult = dutchRailwaysApi.getDeparturesForStation(station.uicCode)
            if (departuresResult is ApiResult.Success) {
                val trainInfo = trainInfoRepository.getTrainInfo(departuresResult.body.map { it.product.number.toInt() }.toSet())
                val associatedDepartures = departuresResult.body.associateWith { departure ->
                    if (!departure.cancelled) {
                        trainInfo.firstOrNull {
                            it.journeyId == departure.product.number.toInt()
                        }
                    } else null
                }
                mutableDepartures.postValue(associatedDepartures.entries.map { it.key to it.value }.toSet())
            } else if (departuresResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(departuresResult.apiError.error)
                departuresResult.apiError.error.printStackTrace()
            }
        }
    }

    fun loadMaintenance(allowReload: Boolean = false) {
        if (!allowReload && !mutableMaintenanceList.value.isNullOrEmpty()) {
            return
        }

        mutableMaintenanceList.postValue(null)

        viewModelScope.launch {
            val disruptionResult = dutchRailwaysApi.getDisruptions(
                isActive = true,
                type = setOf(DisruptionType.MAINTENANCE)
            )

            if(disruptionResult is ApiResult.Success) {
                mutableMaintenanceList.postValue(disruptionResult.body)
            } else if (disruptionResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(disruptionResult.apiError.error)
                disruptionResult.apiError.error.printStackTrace()
            }
        }
    }

    fun loadDisruptions(allowReload: Boolean = false) {
        if (!allowReload && !mutableDisruptions.value.isNullOrEmpty()) {
            return
        }

        mutableDisruptions.postValue(null)

        viewModelScope.launch {
            val disruptionResult = dutchRailwaysApi.getDisruptions(
                isActive = true,
                type = setOf(DisruptionType.CALAMITY, DisruptionType.DISRUPTION)
            )

            if(disruptionResult is ApiResult.Success) {
                mutableDisruptions.postValue(disruptionResult.body)
            } else if (disruptionResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(disruptionResult.apiError.error)
                disruptionResult.apiError.error.printStackTrace()
            }
        }
    }

    companion object {
        private val lastTrainStation = stringPreferencesKey("LAST_TRAIN_STATION")

        private const val DEFAULT_STATION_NAME = "Utrecht Centraal"
    }
}
