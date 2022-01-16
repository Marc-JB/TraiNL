package nl.marc_apps.ovgo.data

import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.type_conversions.TrainInfoConversions
import nl.marc_apps.ovgo.domain.DeviceConfiguration
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.utils.println

class TrainInfoRepository(
    private val dutchRailwaysApi: DutchRailwaysApi,
    private val deviceConfiguration: DeviceConfiguration
) {
    private val trainInfoCache = mutableMapOf<String, TrainInfo>()

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

        val cachedTrainInfoList = mutableListOf<TrainInfo>()
        val notCachedJourneyIds = mutableSetOf<String>()

        for ((id) in ids) {
            val cachedTrainInfo = trainInfoCache[id]
            if (cachedTrainInfo == null) {
                notCachedJourneyIds += id
            } else {
                cachedTrainInfoList += cachedTrainInfo
            }
        }

        println(TAG, "Train info for ${notCachedJourneyIds.size} items fetched from API, train info for ${cachedTrainInfoList.size} items fetched from MEMORY_CACHE")

        val trainInfoList = try {
            dutchRailwaysApi.getTrainInfo(notCachedJourneyIds)
        } catch (error: Throwable) {
            emptyList()
        }

        return cachedTrainInfoList + trainInfoList.map {
            TrainInfoConversions
                .convertApiToDomainModel(it, ids[it.journeyNumber.toString()])
                .also(::addToCache)
        }
    }

    private fun addToCache(trainInfo: TrainInfo) {
        if (trainInfoCache.size >= TRAIN_INFO_MAX_CACHE_SIZE) {
            println(TAG, "Memory limit of $TRAIN_INFO_MAX_CACHE_SIZE reached.")
            trainInfoCache -= trainInfoCache.keys.first()
        }

        if (!deviceConfiguration.isLowRamDevice) {
            trainInfoCache += trainInfo.journeyId.toString() to trainInfo
        }
    }

    companion object {
        private const val TRAIN_INFO_MAX_CACHE_SIZE = 60

        private const val TAG = "TRAIN_INFO_REPO"
    }
}
