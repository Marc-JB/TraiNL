package nl.marc_apps.ovgo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.dependency_injection.netComponent
import nl.marc_apps.ovgo.domainmodels.Disruption
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import javax.inject.Inject

class DisruptionsViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var dataRepository: PublicTransportDataRepository

    val disruptions: MutableList<Disruption> = mutableListOf()

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
            disruptions.clear()
            disruptions.addAll(dataRepository.getDisruptions())
            _isLoading.postValue(false)
        }
    }

    init {
        netComponent.inject(this)
    }
}
