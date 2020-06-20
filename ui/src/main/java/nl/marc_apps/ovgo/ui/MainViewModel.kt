package nl.marc_apps.ovgo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class MainViewModel(private val dataRepository: PublicTransportDataRepository) : ViewModel() {
    val disruptionCount: MutableLiveData<Int> = MutableLiveData(-1)

    init {
        viewModelScope.launch {
            val disruptions = async {
                dataRepository.getDisruptions(true)
            }
            disruptionCount.postValue(disruptions.await().size)
        }
    }
}
