package nl.marc_apps.ovgo.ui.departures

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class DeparturesViewModel(private val dataRepository: PublicTransportDataRepository) : ViewModel(), DeparturesViewModelInf {
    override val departures: MutableLiveData<Array<Departure>> = MutableLiveData(emptyArray())

    override val isLoading = MutableLiveData(true)

    override var languageCode: String = "en"
        set(value){
            field = value
            dataRepository.language = value
        }

    override val station: MutableLiveData<String> = MutableLiveData()

    fun load(lifecycleOwner: LifecycleOwner) {
        station.observe(lifecycleOwner) {
            viewModelScope.launch {
                loadStations(it)
            }
        }
    }

    suspend fun loadStations(stationId: String) = coroutineScope {
        isLoading.postValue(true)
        val departuresList = async(coroutineContext) {
            dataRepository.getDepartures(stationId)
        }
        departures.postValue(departuresList.await())
        isLoading.postValue(false)
    }
}
