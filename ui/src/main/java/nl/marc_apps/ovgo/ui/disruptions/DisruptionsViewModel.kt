package nl.marc_apps.ovgo.ui.disruptions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    var languageCode: String? = null
        set(value){
            field = value
            if (value != null) {
                dataRepository.language = value
                loadDisruptions()
            }
        }

    private fun loadDisruptions() {
        if (languageCode != null && disruptions.value.isNullOrEmpty()) {
            Log.w("APP", "(Re)loading disruptions")
            viewModelScope.launch {
                _isLoading.postValue(true)
                _disruptions.postValue(dataRepository.getDisruptions())
                _isLoading.postValue(false)
            }
        }
    }

    init {
        loadDisruptions()
    }
}
