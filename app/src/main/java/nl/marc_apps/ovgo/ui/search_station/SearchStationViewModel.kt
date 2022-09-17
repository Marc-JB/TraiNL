package nl.marc_apps.ovgo.ui.search_station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.search.StringSimilarity

class SearchStationViewModel(
    private val trainStationRepository: TrainStationRepository,
    private val stringSimilarity: StringSimilarity
) : ViewModel() {
    private var allStations = emptyList<TrainStation>()

    private val mutableIsLoadingData = MutableStateFlow(true)

    val isLoadingData: StateFlow<Boolean>
        get() = mutableIsLoadingData

    private val mutableStationSuggestions = MutableStateFlow(emptyList<TrainStation>())

    val stationSuggestions: StateFlow<List<TrainStation>>
        get() = mutableStationSuggestions

    init {
        loadAllStations()
    }

    private fun loadAllStations() {
        mutableIsLoadingData.value = true

        viewModelScope.launch {
            allStations = trainStationRepository.getTrainStations()
            mutableIsLoadingData.value = false
        }
    }

    fun updateAutocompleteList(stationQuery: String) {
        mutableStationSuggestions.value = allStations.sortedByDescending { station ->
            val allNames = station.alternativeNames + station.alternativeSearches + station.fullName + station.shortenedName

            allNames.maxOf {
                stringSimilarity.calculateSimilarity(stationQuery.lowercase(), it.lowercase())
            }
        }
    }
}
