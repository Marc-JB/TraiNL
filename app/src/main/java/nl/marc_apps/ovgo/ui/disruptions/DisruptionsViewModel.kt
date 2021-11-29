package nl.marc_apps.ovgo.ui.disruptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionType

class DisruptionsViewModel(
    private val dutchRailwaysApi: DutchRailwaysApi
) : ViewModel() {
    private val mutableDisruptions = MutableLiveData<List<DutchRailwaysDisruption>?>()

    val disruptions: LiveData<List<DutchRailwaysDisruption>?>
        get() = mutableDisruptions

    fun loadDisruptions(allowReload: Boolean = false) {
        if (!allowReload && !mutableDisruptions.value.isNullOrEmpty()) {
            return
        }

        mutableDisruptions.postValue(null)

        viewModelScope.launch {
            val disruptions = try {
                dutchRailwaysApi.getDisruptions(
                    isActive = true,
                    type = setOf(DisruptionType.CALAMITY, DisruptionType.DISRUPTION)
                )
            } catch (error: Throwable) {
                emptyList()
            }

            mutableDisruptions.postValue(disruptions)
        }
    }
}
