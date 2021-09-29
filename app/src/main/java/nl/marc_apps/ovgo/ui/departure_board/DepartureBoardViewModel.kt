package nl.marc_apps.ovgo.ui.departure_board

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.DepartureRepository
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation
import java.util.*

class DepartureBoardViewModel(
    private val trainStationRepository: TrainStationRepository,
    private val departureRepository: DepartureRepository,
    private val preferences: DataStore<Preferences>
) : ViewModel() {
    private val mutableCurrentStation = MutableLiveData<TrainStation>()

    val currentStation: LiveData<TrainStation>
        get() = mutableCurrentStation

    private val mutableDepartures = MutableLiveData<Set<Departure>?>()

    val departures: LiveData<Set<Departure>?>
        get() = mutableDepartures

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
            val departures = departureRepository.getDepartures(station)
            mutableDepartures.postValue(departures)
        }
    }

    companion object {
        private val lastTrainStation = stringPreferencesKey("LAST_TRAIN_STATION")

        private const val DEFAULT_STATION_NAME = "Utrecht Centraal"
    }
}
