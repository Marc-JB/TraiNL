package nl.marc_apps.ovgo.ui.departure_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.JourneyDetailsRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.JourneyStop

class DepartureDetailsViewModel(
    private val journeyDetailsRepository: JourneyDetailsRepository
) : ViewModel() {
    private val mutableJourneyStops = MutableLiveData<List<JourneyStop>?>(null)

    val journeyStops: LiveData<List<JourneyStop>?>
        get() = mutableJourneyStops

    fun loadStations(departure: Departure) {
        viewModelScope.launch {
            try {
                val stops = journeyDetailsRepository.getStops(departure.journeyId).toList()

                if (stops.isNotEmpty()) {
                    mutableJourneyStops.postValue(stops)
                }
            } catch (ignored: Throwable) {}
        }
    }
}
