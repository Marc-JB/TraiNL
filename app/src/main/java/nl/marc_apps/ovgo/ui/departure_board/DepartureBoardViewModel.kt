package nl.marc_apps.ovgo.ui.departure_board

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.PreferenceKeys
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.getOrNull

class DepartureBoardViewModel(
    state: SavedStateHandle,
    private val trainStationRepository: TrainStationRepository,
    private val departureRepository: DepartureRepository,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private val mutableCurrentStation = state.getLiveData<TrainStation>(SAVED_STATE_KEY_STATION)

    val currentStation: LiveData<TrainStation>
        get() = mutableCurrentStation

    private val mutableDepartures = MutableLiveData<Result<List<Departure>>?>()

    val departures: LiveData<Result<List<Departure>>?>
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

        private const val SAVED_STATE_KEY_STATION = "KEY_STATION"
    }
}
