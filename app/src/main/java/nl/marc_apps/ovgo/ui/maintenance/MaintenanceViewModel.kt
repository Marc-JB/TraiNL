package nl.marc_apps.ovgo.ui.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption.DisruptionType

class MaintenanceViewModel(
    private val dutchRailwaysApi: DutchRailwaysApi
) : ViewModel() {
    private val mutableMaintenanceList = MutableStateFlow<List<DutchRailwaysDisruption>?>(null)

    val maintenanceList: StateFlow<List<DutchRailwaysDisruption>?>
        get() = mutableMaintenanceList

    fun loadMaintenance(allowReload: Boolean = false) {
        if (!allowReload && !mutableMaintenanceList.value.isNullOrEmpty()) {
            return
        }

        mutableMaintenanceList.value = null

        viewModelScope.launch {
            val maintenanceList = try {
                dutchRailwaysApi.getDisruptions(
                    isActive = true,
                    type = setOf(DisruptionType.MAINTENANCE)
                )
            } catch (error: Throwable) {
                emptyList()
            }

            mutableMaintenanceList.value = maintenanceList
        }
    }
}
