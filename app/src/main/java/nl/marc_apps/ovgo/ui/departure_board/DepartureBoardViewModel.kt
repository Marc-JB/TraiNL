package nl.marc_apps.ovgo.ui.departure_board

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.PreferenceKeys
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.getOrNull

class DepartureBoardViewModel(
    private val trainStationRepository: TrainStationRepository,
    private val departureRepository: DepartureRepository,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private var mutableCurrentStation = MutableStateFlow<TrainStation?>(null)

    val currentStation: StateFlow<TrainStation?>
        get() = mutableCurrentStation

    private val mutableDepartures = MutableStateFlow<Result<List<Departure>>?>(null)

    val departures: StateFlow<Result<List<Departure>>?>
        get() = mutableDepartures

    private fun saveCurrentStation(station: TrainStation) {
        viewModelScope.launch {
            preferences.edit {
                it[PreferenceKeys.lastTrainStation] = station.uicCode
            }
        }
    }

    private suspend fun getDefaultTrainStation(): TrainStation? {
        return trainStationRepository.getTrainStations().firstOrNull {
            it.fullName == DEFAULT_STATION_NAME
        }
    }

    private suspend fun getSavedTrainStation(): TrainStation? {
        return preferences.getOrNull(PreferenceKeys.lastTrainStation)?.let {
            trainStationRepository.getTrainStationById(it)
        }
    }

    fun loadDeparturesForLastKnownStation() {
        viewModelScope.launch {
            (currentStation.value ?: getSavedTrainStation() ?: getDefaultTrainStation())?.let {
                loadDepartures(it)
            }
        }
    }

    fun loadDepartures(stationId: String, allowReload: Boolean = false) {
        if (!allowReload && departures.value?.isSuccess == true) {
            return
        }

        val currentStation = currentStation.value
        if (currentStation?.uicCode == stationId) {
            loadDepartures(currentStation, allowReload)
            return
        }

        viewModelScope.launch {
            val station = trainStationRepository.getTrainStationById(stationId) ?: return@launch
            loadDepartures(station, allowReload)
        }
    }

    fun loadDepartures(station: TrainStation, allowReload: Boolean = false) {
        if (!allowReload && departures.value?.isSuccess == true) {
            return
        }

        if (currentStation.value != station) {
            mutableCurrentStation.value = station
            saveCurrentStation(station)
        }
        mutableDepartures.value = null

        viewModelScope.launch {
            val departures = runCatching {
                departureRepository.getDepartures(station)
            }
            mutableDepartures.value = departures
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
