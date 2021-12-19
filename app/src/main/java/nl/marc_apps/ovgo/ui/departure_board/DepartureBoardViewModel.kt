package nl.marc_apps.ovgo.ui.departure_board

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.PreferenceKeys
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.getOrNull
import java.util.*

class DepartureBoardViewModel(
    private val trainStationRepository: TrainStationRepository,
    private val departureRepository: DepartureRepository,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private val mutableCurrentStation = MutableLiveData<TrainStation>()

    val currentStation: LiveData<TrainStation>
        get() = mutableCurrentStation

    private val mutableDepartures = MutableLiveData<Result<List<Departure>>?>()

    val departures: LiveData<Result<List<Departure>>?>
        get() = mutableDepartures

    fun saveCurrentStation(station: TrainStation? = currentStation.value) {
        if (station == null) return

        viewModelScope.launch {
            preferences.edit {
                it[PreferenceKeys.lastTrainStation] = station.uicCode
            }
        }
    }

    fun loadDeparturesForLastKnownStation() {
        viewModelScope.launch {
            val lastKnownStationId = preferences.getOrNull(PreferenceKeys.lastTrainStation)
            if (lastKnownStationId != null) {
                val station = trainStationRepository.getTrainStationById(lastKnownStationId)
                if (station != null) {
                    loadDepartures(station)
                    return@launch
                }
            }

            val station = trainStationRepository.getTrainStations().firstOrNull {
                it.fullName == DEFAULT_STATION_NAME
            }
            if (station != null) {
                loadDepartures(station)
            }
        }
    }

    fun loadDepartures(station: TrainStation, allowReload: Boolean = false) {
        if (!allowReload && departures.value?.isSuccess == true) {
            return
        }

        if (currentStation.value != station) {
            mutableCurrentStation.postValue(station)
            saveCurrentStation(station)
        }
        mutableDepartures.postValue(null)

        viewModelScope.launch {
            val departures = runCatching {
                departureRepository.getDepartures(station)
            }
            mutableDepartures.postValue(departures)
        }
    }

    fun reload() {
        val currentStation = currentStation.value
        if (currentStation != null) {
            loadDepartures(currentStation)
        } else {
            loadDeparturesForLastKnownStation()
        }
    }

    companion object {
        private const val DEFAULT_STATION_NAME = "Utrecht Centraal"
    }
}
