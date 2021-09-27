package nl.marc_apps.ovgo.data

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nl.marc_apps.ovgo.data.api.dutch_railways.DutchRailwaysApi
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.utils.ApiResult

class TrainInfoRepository(
    private val dutchRailwaysApi: DutchRailwaysApi
) {
    suspend fun getTrainInfo(ids: Set<Int>): Set<TrainInfo> {
        val trainInfoResult = dutchRailwaysApi.getTrainInfo(ids)
        if (trainInfoResult is ApiResult.Success) {
            return trainInfoResult.body.map {
                it.asTrainInfo()
            }.toSet()
        } else if (trainInfoResult is ApiResult.Failure) {
            Firebase.crashlytics.recordException(trainInfoResult.apiError.error)
            trainInfoResult.apiError.error.printStackTrace()
        }

        return emptySet()
    }
}
