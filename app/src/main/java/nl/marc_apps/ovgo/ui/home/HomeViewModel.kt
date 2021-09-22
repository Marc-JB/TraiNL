package nl.marc_apps.ovgo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.marc_apps.ovgo.data.TrainStationRepository
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.ApiResult

class HomeViewModel(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val trainStationRepository: TrainStationRepository
) : ViewModel() {
    private val mutableCurrentStationName = MutableLiveData<String?>()

    val currentStationName: LiveData<String?>
        get() = mutableCurrentStationName

    private val mutableDepartures = MutableLiveData<Set<Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>>?>()

    val departures: LiveData<Set<Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>>?>
        get() = mutableDepartures

    private val mutableDisruptions = MutableLiveData<Set<DutchRailwaysDisruption.DisruptionOrMaintenance>?>()

    val disruptions: LiveData<Set<DutchRailwaysDisruption.DisruptionOrMaintenance>?>
        get() = mutableDisruptions

    private val mutableMaintenanceList = MutableLiveData<Set<DutchRailwaysDisruption.DisruptionOrMaintenance>?>()

    val maintenanceList: LiveData<Set<DutchRailwaysDisruption.DisruptionOrMaintenance>?>
        get() = mutableMaintenanceList

    fun loadDepartures(stationName: String, allowReload: Boolean = false) {
        viewModelScope.launch {
            val station = trainStationRepository.getTrainStations().firstOrNull {
                it.name == stationName
            }
            if (station != null) {
                loadDepartures(station, allowReload)
            }
        }
    }

    fun loadDepartures(station: TrainStation, allowReload: Boolean = false) {
        if (!allowReload && !mutableDepartures.value.isNullOrEmpty()) {
            return
        }

        mutableCurrentStationName.postValue(station.name)
        mutableDepartures.postValue(null)

        viewModelScope.launch {
            val departuresResult = dutchRailwaysApi.getDeparturesForStation(station.uicCode)
            if (departuresResult is ApiResult.Success) {
                val map = mutableSetOf<Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>>()
                for (item in departuresResult.body.take(12)) {
                    if (item.cancelled) {
                        map += item to null
                    } else {
                        delay(100)
                        val trainInfoResult = dutchRailwaysApi.getTrainInfo(item.product.number.toInt())

                        if (trainInfoResult is ApiResult.Success) {
                            map += item to trainInfoResult.body
                        } else if (trainInfoResult is ApiResult.Failure) {
                            trainInfoResult.apiError.error.printStackTrace()
                        }

                        trainInfoResult.bodyOrNull?.let {
                            map += item to it
                        }
                    }
                }
                mutableDepartures.postValue(map)
            } else if (departuresResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(departuresResult.apiError.error)
                departuresResult.apiError.error.printStackTrace()
            }
        }
    }

    fun loadDisruptionsAndMaintenance(allowReload: Boolean = false) {
        if (!allowReload && (!mutableDisruptions.value.isNullOrEmpty() || !mutableMaintenanceList.value.isNullOrEmpty())) {
            return
        }

        mutableDisruptions.postValue(null)
        mutableMaintenanceList.postValue(null)

        viewModelScope.launch {
            val disruptionResult = dutchRailwaysApi.getDisruptions(isActive = true)

            if(disruptionResult is ApiResult.Success) {
                val disruptions = disruptionResult.body
                val disruptionOrMaintenanceList = disruptions.filterIsInstance<DutchRailwaysDisruption.DisruptionOrMaintenance>()

                mutableDisruptions.postValue(disruptionOrMaintenanceList.filter {
                    it.type == DutchRailwaysDisruption.DisruptionType.DISRUPTION
                }.toSet())

                mutableMaintenanceList.postValue(disruptionOrMaintenanceList.filter {
                    it.type == DutchRailwaysDisruption.DisruptionType.MAINTENANCE
                }.toSet())
            } else if (disruptionResult is ApiResult.Failure) {
                Firebase.crashlytics.recordException(disruptionResult.apiError.error)
                disruptionResult.apiError.error.printStackTrace()
            }
        }
    }
}
