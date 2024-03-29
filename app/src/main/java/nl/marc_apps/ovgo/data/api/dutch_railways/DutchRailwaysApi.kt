package nl.marc_apps.ovgo.data.api.dutch_railways

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nl.marc_apps.ovgo.data.api.dutch_railways.models.*
import retrofit2.await

class DutchRailwaysApi(
    private val travelInfoApi: DutchRailwaysTravelInfoApi,
    private val trainInfoApi: DutchRailwaysTrainInfoApi,
    private val dutchRailwaysTravelInfoApiKey: String,
    private val defaultLanguageCode: String? = null
) {
    suspend fun getDisruptions(
        language: String? = defaultLanguageCode,
        type: Set<DutchRailwaysDisruption.DisruptionType>? = null,
        isActive: Boolean? = null
    ): List<DutchRailwaysDisruption> {
        if (type?.isEmpty() == true) {
            return emptyList()
        }

        try {
            return travelInfoApi.getDisruptions(
                dutchRailwaysTravelInfoApiKey,
                language = language,
                isActive = isActive,
                type = type?.joinToString(separator = ",") { it.name }
            ).await()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getTrainStations(): List<DutchRailwaysStation> {
        try {
            return travelInfoApi.getStations(
                apiKey = dutchRailwaysTravelInfoApiKey
            ).await().payload?.filterNotNull() ?: emptyList()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getDeparturesForStation(
        uicCode: String,
        language: String? = defaultLanguageCode
    ): List<DutchRailwaysDeparture> {
        try {
            return travelInfoApi.getDeparturesByUicCode(
                dutchRailwaysTravelInfoApiKey,
                uicCode = uicCode,
                language = language
            ).await().payload?.departures?.filterNotNull() ?: emptyList()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getTrainInfo(journeyNumber: String): DutchRailwaysTrainInfo? {
        try {
            return trainInfoApi.getTrainInfo(
                dutchRailwaysTravelInfoApiKey,
                journeyNumber = journeyNumber
            ).await()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getTrainInfo(journeys: Set<String>): List<DutchRailwaysTrainInfo> {
        if (journeys.isEmpty()) {
            return emptyList()
        }

        try {
            return trainInfoApi.getTrainInfo(
                dutchRailwaysTravelInfoApiKey,
                ids = journeys.joinToString(separator = ",")
            ).await().filterNotNull()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getJourneyDetails(journeyNumber: String): Set<DutchRailwaysStop> {
        try {
            return travelInfoApi.getJourneyDetails(
                dutchRailwaysTravelInfoApiKey,
                journeyNumber = journeyNumber
            ).await().payload?.stops ?: emptySet()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }
}
