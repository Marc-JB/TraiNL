package nl.marc_apps.ovgo.data.api.dutch_railways

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nl.marc_apps.ovgo.data.api.HttpClient
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStation
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.utils.retrofit
import retrofit2.await

class DutchRailwaysApi(
    private val httpClient: HttpClient,
    private val dutchRailwaysTravelInfoApiKey: String,
    private val defaultLanguageCode: String? = null
) {
    private val travelInfoApi = retrofit<DutchRailwaysTravelInfoApi> {
        baseUrl(DutchRailwaysTravelInfoApi.BASE_URL)
        addConverterFactory(httpClient.jsonConverter)
        client(httpClient.okHttpClient)
    }

    private val trainInfoApi = retrofit<DutchRailwaysTrainInfoApi> {
        baseUrl(DutchRailwaysTrainInfoApi.BASE_URL)
        addConverterFactory(httpClient.jsonConverter)
        client(httpClient.okHttpClient)
    }

    suspend fun getDisruptions(
        language: String? = defaultLanguageCode,
        type: Set<DutchRailwaysDisruption.DisruptionType>? = null,
        isActive: Boolean? = null
    ): List<DutchRailwaysDisruption> {
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
            return travelInfoApi.getStations(dutchRailwaysTravelInfoApiKey).await().payload
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
            ).await().payload.departures.filterNotNull()
        } catch (error: Throwable) {
            Firebase.crashlytics.recordException(error)
            error.printStackTrace()
            throw error
        }
    }

    suspend fun getTrainInfo(journeyNumber: Int): DutchRailwaysTrainInfo? {
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

    suspend fun getTrainInfo(journeys: Set<Int>): List<DutchRailwaysTrainInfo> {
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
}
