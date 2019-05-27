package nl.marc_apps.ovgo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.models.Departure
import nl.marc_apps.ovgo.repositories.NsRepository

class DeparturesViewModel : ViewModel() {
    val departures: MutableList<Departure> = mutableListOf()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var languageCode: String = "en"

    private val dataRepository
            get() = NsRepository(languageCode)

    var stationCode: String? = null
        set(value){
            if(field != value && value != null) {
                field = value
                updateDepartures(value)
            }
        }

    private fun updateDepartures(stationId: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            departures.clear()
            departures.addAll(dataRepository.getDepartures(stationId))
            _isLoading.postValue(false)
        }
    }
}
