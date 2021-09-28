package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.domain.TrainInfo

class TrainInfoRepository(
    private val dutchRailwaysApi: DutchRailwaysApi
) {
    suspend fun getTrainInfo(ids: Set<Int>): Set<TrainInfo> {
        if (ids.isEmpty()) {
            return emptySet()
        }

        val trainInfoList = try {
            dutchRailwaysApi.getTrainInfo(ids)
        } catch (error: Throwable) {
            emptySet()
        }

        return trainInfoList.map {
            it.asTrainInfo()
        }.toSet()
    }
}
