package nl.marc_apps.ovgo.ui.disruptions

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import nl.marc_apps.ovgo.domain.services.UserPreferences

class DisruptionsViewModel(
    private val dataRepository: PublicTransportDataRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val mutableDisruptions = MutableLiveData(emptySet<Disruption>())

    val disruptions: LiveData<Set<Disruption>>
        get() = mutableDisruptions

    private val mutableIsLoading = MutableLiveData(true)

    val isLoading: LiveData<Boolean>
        get() = mutableIsLoading

    private fun loadDisruptions() {
        if (disruptions.value.isNullOrEmpty()) {
            Log.w("APP", "(Re)loading disruptions")
            viewModelScope.launch {
                mutableIsLoading.postValue(true)
                mutableDisruptions.postValue(dataRepository.getDisruptions())
                mutableIsLoading.postValue(false)
            }
        }
    }

    init {
        dataRepository.language = userPreferences.language
        loadDisruptions()
    }
}
