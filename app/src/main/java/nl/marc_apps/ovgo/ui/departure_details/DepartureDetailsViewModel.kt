package nl.marc_apps.ovgo.ui.departure_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.JourneyDetailsRepository
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.JourneyStop

class DepartureDetailsViewModel(
    private val journeyDetailsRepository: JourneyDetailsRepository
) : ViewModel() {
    private val mutableJourneyStops = MutableStateFlow<List<JourneyStop>?>(null)

    val journeyStops: StateFlow<List<JourneyStop>?>
        get() = mutableJourneyStops

    fun loadStations(departure: Departure) {
        if (departure.isForeignService) {
            mutableJourneyStops.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                mutableJourneyStops.value = journeyDetailsRepository.getStops(departure.journeyId).toList()
            } catch (error: Throwable) {
                mutableJourneyStops.value = emptyList()
            }
        }
    }
}
