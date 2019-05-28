package nl.marc_apps.ovgo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domainmodels.Departure
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import nl.marc_apps.ovgo.repositories.OVgoApiRepository

class DeparturesViewModel : ViewModel() {
    val departures: MutableList<Departure> = mutableListOf()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var languageCode: String = "en"

    private val dataRepository: PublicTransportDataRepository
        get() = OVgoApiRepository(languageCode)

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
