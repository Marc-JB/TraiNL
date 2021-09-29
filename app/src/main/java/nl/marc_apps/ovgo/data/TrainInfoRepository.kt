package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.type_conversions.TrainInfoConversions
import nl.marc_apps.ovgo.domain.TrainInfo

class TrainInfoRepository(
    private val dutchRailwaysApi: DutchRailwaysApi
) {
    suspend fun getTrainInfo(ids: Set<Int>): List<TrainInfo> {
        if (ids.isEmpty()) {
            return emptyList()
        }

        val trainInfoList = try {
            dutchRailwaysApi.getTrainInfo(ids)
        } catch (error: Throwable) {
            emptyList()
        }

        return trainInfoList.map {
            TrainInfoConversions.convertApiToDomainModel(it)
        }
    }
}
