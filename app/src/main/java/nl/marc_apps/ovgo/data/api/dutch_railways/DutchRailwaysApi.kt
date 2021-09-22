package nl.marc_apps.ovgo.data.api.dutch_railways

import nl.marc_apps.ovgo.data.api.HttpClient
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysStation
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.utils.ApiResult
import nl.marc_apps.ovgo.utils.awaitResult
import nl.marc_apps.ovgo.utils.retrofit

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
    ): ApiResult<Set<DutchRailwaysDisruption>> {
        // TODO: Add implementation for the type parameter
        return travelInfoApi.getDisruptions(
            dutchRailwaysTravelInfoApiKey,
            language = language,
            isActive = isActive
        ).awaitResult()
    }

    suspend fun getTrainStations(): ApiResult<Set<DutchRailwaysStation>> {
        return travelInfoApi.getStations(dutchRailwaysTravelInfoApiKey)
            .awaitResult()
            .mapSuccess {
                it.payload
            }
    }

    suspend fun getDeparturesForStation(
        uicCode: String,
        language: String? = defaultLanguageCode
    ): ApiResult<Set<DutchRailwaysDeparture>> {
        return travelInfoApi.getDeparturesByUicCode(
            dutchRailwaysTravelInfoApiKey,
            uicCode = uicCode,
            language = language
        ).awaitResult().mapSuccess {
            it.payload.departures
        }
    }

    suspend fun getTrainInfo(journeyNumber: Int): ApiResult<DutchRailwaysTrainInfo> {
        return trainInfoApi.getTrainInfo(
            dutchRailwaysTravelInfoApiKey,
            journeyNumber = journeyNumber
        ).awaitResult()
    }
}
