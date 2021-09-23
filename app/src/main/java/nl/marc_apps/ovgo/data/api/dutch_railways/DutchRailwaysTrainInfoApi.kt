package nl.marc_apps.ovgo.data.api.dutch_railways

import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DutchRailwaysTrainInfoApi {
    @GET("v1/trein/{journeyNumber}")
    fun getTrainInfo(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Path("journeyNumber") journeyNumber: Int,
        @Query("features") features: String? = null
    ): Call<DutchRailwaysTrainInfo>

    @GET("v1/trein")
    fun getTrainInfo(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("ids") ids: String,
        @Query("features") features: String? = null,
        @Query("all") informationAboutAllStations: Boolean? = null
    ): Call<Set<DutchRailwaysTrainInfo>>

    companion object {
        const val BASE_URL = "https://gateway.apiportal.ns.nl/virtual-train-api/api/"
    }
}
