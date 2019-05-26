package nl.marc_apps.ovgo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.models.Disruption
import nl.marc_apps.ovgo.repositories.NsRepository

class DisruptionsViewModel : ViewModel() {
    val disruptions: MutableList<Disruption> = mutableListOf()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var languageCode: String = "en"

    private val dataRepository
            get() = NsRepository(languageCode)

    fun loadDisruptions() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            disruptions.clear()
            disruptions.addAll(dataRepository.getDisruptions())
            _isLoading.postValue(false)
        }
    }
}
