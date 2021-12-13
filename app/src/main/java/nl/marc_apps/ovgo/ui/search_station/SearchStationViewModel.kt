package nl.marc_apps.ovgo.ui.search_station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.search.StringSimilarity

class SearchStationViewModel(
    private val trainStationRepository: TrainStationRepository,
    private val stringSimilarity: StringSimilarity
) : ViewModel() {
    private var allStations = emptyList<TrainStation>()

    private val mutableIsLoadingData = MutableLiveData(true)

    val isLoadingData: LiveData<Boolean>
        get() = mutableIsLoadingData

    private val mutableStationSuggestions = MutableLiveData(emptyList<TrainStation>())

    val stationSuggestions: LiveData<List<TrainStation>>
        get() = mutableStationSuggestions

    init {
        loadAllStations()
    }

    private fun loadAllStations() {
        mutableIsLoadingData.postValue(true)

        viewModelScope.launch {
            allStations = trainStationRepository.getTrainStations()
            mutableIsLoadingData.postValue(false)
        }
    }

    fun updateAutocompleteList(stationQuery: String) {
        mutableStationSuggestions.postValue(allStations.sortedByDescending { station ->
            val allNames = station.alternativeNames + station.alternativeSearches + station.fullName + station.shortenedName

            allNames.maxOf {
                stringSimilarity.calculateSimilarity(stationQuery.lowercase(), it.lowercase())
            }
        }.take(AUTOCOMPLETE_SUGGESTION_LIST_SIZE))
    }

    companion object {
        private const val AUTOCOMPLETE_SUGGESTION_LIST_SIZE = 15
    }
}
