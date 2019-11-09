package nl.marc_apps.ovgo.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository

class DisruptionsViewModel(private val dataRepository: PublicTransportDataRepository) : ViewModel() {
    private val _disruptions: MutableLiveData<Array<Disruption>> = MutableLiveData(emptyArray())
    val disruptions: LiveData<Array<Disruption>>
        get() = _disruptions

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var languageCode: String = "en"
        set(value){
            field = value
            dataRepository.language = value
        }

    fun loadDisruptions() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _disruptions.postValue(dataRepository.getDisruptions())
            _isLoading.postValue(false)
        }
    }
}
