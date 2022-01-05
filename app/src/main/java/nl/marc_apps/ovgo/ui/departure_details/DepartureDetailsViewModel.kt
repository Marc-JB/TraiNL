package nl.marc_apps.ovgo.ui.departure_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.JourneyDetailsRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainStation

class DepartureDetailsViewModel(
    private val journeyDetailsRepository: JourneyDetailsRepository
) : ViewModel() {
    private val mutableRouteStations = MutableLiveData<List<TrainStation>>()

    val routeStations: LiveData<List<TrainStation>>
        get() = mutableRouteStations

    fun loadStations(departure: Departure) {
        mutableRouteStations.postValue(departure.stationsOnRoute)

        viewModelScope.launch {
            try {
                val stops = journeyDetailsRepository.getStops(departure.journeyId).toList()

                if (stops.isNotEmpty()) {
                    mutableRouteStations.postValue(stops)
                }
            } catch (ignored: Throwable) {}
        }
    }
}
