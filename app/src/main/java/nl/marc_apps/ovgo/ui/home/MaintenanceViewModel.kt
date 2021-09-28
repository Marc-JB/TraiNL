package nl.marc_apps.ovgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionType
import nl.marc_apps.ovgo.utils.ApiResult

class MaintenanceViewModel(
    private val dutchRailwaysApi: DutchRailwaysApi
) : ViewModel() {
    private val mutableMaintenanceList = MutableLiveData<Set<DutchRailwaysDisruption>?>()

    val maintenanceList: LiveData<Set<DutchRailwaysDisruption>?>
        get() = mutableMaintenanceList

    fun loadMaintenance(allowReload: Boolean = false) {
        if (!allowReload && !mutableMaintenanceList.value.isNullOrEmpty()) {
            return
        }

        mutableMaintenanceList.postValue(null)

        viewModelScope.launch {
            val disruptionResult = dutchRailwaysApi.getDisruptions(
                isActive = true,
                type = setOf(DisruptionType.MAINTENANCE)
            )

            if(disruptionResult is ApiResult.Success) {
                mutableMaintenanceList.postValue(disruptionResult.body)
            } else if (disruptionResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(disruptionResult.apiError.error)
                disruptionResult.apiError.error.printStackTrace()
            }
        }
    }
}
