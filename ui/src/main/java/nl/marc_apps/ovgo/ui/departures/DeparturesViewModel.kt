package nl.marc_apps.ovgo.ui.departures

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class DeparturesViewModel(
    private val dataRepository: PublicTransportDataRepository
) : ViewModel(), DeparturesViewModelInf {
    override val departures: MutableLiveData<Array<Departure>> = MutableLiveData(emptyArray())

    override val isLoading = MutableLiveData(true)

    override var languageCode: String = "en"
        set(value){
            field = value
            dataRepository.language = value
        }

    override val station: MutableLiveData<String> = MutableLiveData()

    private val stationObserver = Observer<String> {
        loadDepartures(it)
    }

    init {
        station.observeForever(stationObserver)
    }

    override fun onCleared() {
        station.removeObserver(stationObserver)
        super.onCleared()
    }

    fun loadDepartures(station: String) {
        if(departures.value.isNullOrEmpty()) {
            Log.w("APP", "(Re)loading departures")
            viewModelScope.launch {
                isLoading.postValue(true)
                val departuresList = async(coroutineContext) {
                    dataRepository.getDepartures(station)
                }
                departures.postValue(departuresList.await())
                isLoading.postValue(false)
            }
        }
    }
}
