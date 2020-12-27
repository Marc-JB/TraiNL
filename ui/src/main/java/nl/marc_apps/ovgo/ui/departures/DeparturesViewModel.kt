package nl.marc_apps.ovgo.ui.departures

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.domain.services.UserPreferences

class DeparturesViewModel(
    private val dataRepository: PublicTransportDataRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val mutableDepartures = MutableLiveData(emptySet<Departure>())

    val departures: LiveData<Set<Departure>>
        get() = mutableDepartures

    private val mutableIsLoading = MutableLiveData(false)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    val station = MutableLiveData(userPreferences.station)

    private var lastStation: String? = null

    init {
        dataRepository.language = userPreferences.language
    }

    fun loadDepartures() {
        val stationToLoad = station.value
        if(lastStation != stationToLoad && stationToLoad != null && isLoading.value == false) {
            Log.w("APP", "(Re)loading departures")
            mutableIsLoading.postValue(true)
            viewModelScope.launch {
                val departuresList = dataRepository.getDepartures(stationToLoad)
                mutableDepartures.postValue(departuresList)
                lastStation = stationToLoad
                mutableIsLoading.postValue(false)
            }
        }
    }
}
