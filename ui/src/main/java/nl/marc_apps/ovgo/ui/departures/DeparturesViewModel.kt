package nl.marc_apps.ovgo.ui.departures

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import kotlinx.coroutines.*
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class DeparturesViewModel(val dataRepository: PublicTransportDataRepository) : ViewModel(), DeparturesViewModelInf, CoroutineScope by MainScope() {
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
            launch {
                isLoading.postValue(true)
                val departuresList = async {
                    dataRepository.getDepartures(it)
                }
                departures.postValue(departuresList.await())
                isLoading.postValue(false)
            }
        }
    }
}
