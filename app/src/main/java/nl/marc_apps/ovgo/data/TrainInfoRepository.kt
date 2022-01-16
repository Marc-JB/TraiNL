package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.type_conversions.TrainInfoConversions
import nl.marc_apps.ovgo.domain.TrainInfo

class TrainInfoRepository(private val dutchRailwaysApi: DutchRailwaysApi) {
    suspend fun getTrainInfo(departures: List<DutchRailwaysDeparture>): List<TrainInfo> {
        return getTrainInfo(departures.associateBy { it.product.number })
    }

    suspend fun getTrainInfo(ids: Set<String>): List<TrainInfo> {
        return getTrainInfo(ids.associateWith { null })
    }

    private suspend fun getTrainInfo(ids: Map<String, DutchRailwaysDeparture?>): List<TrainInfo> {
        if (ids.isEmpty()) {
            return emptyList()
        }

        val trainInfoList = try {
            dutchRailwaysApi.getTrainInfo(ids.keys)
        } catch (error: Throwable) {
            emptyList()
        }

        return trainInfoList.map {
            TrainInfoConversions.convertApiToDomainModel(it, ids[it.journeyNumber.toString()])
        }
    }

    companion object {
        private const val TAG = "TRAIN_INFO_REPO"
    }
}
