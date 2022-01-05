package nl.marc_apps.ovgo.data.api.dutch_railways

import nl.marc_apps.ovgo.data.api.dutch_railways.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DutchRailwaysTravelInfoApi {
    @GET("v3/disruptions")
    fun getDisruptions(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Header("Accept-Language") language: String? = null,
        @Query("type") type: String? = null,
        @Query("isActive") isActive: Boolean? = null
    ): Call<List<DutchRailwaysDisruption>>

    @GET("v2/departures")
    fun getDeparturesByUicCode(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("uicCode") uicCode: String,
        @Query("lang") language: String? = null
    ): Call<Payload<Departures>>

    @GET("v2/arrivals")
    fun getArrivalsByUicCode(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("uicCode") uicCode: String,
        @Query("lang") language: String? = null
    ): Call<Payload<Arrivals>>

    @GET("v2/stations")
    fun getStations(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String
    ): Call<Payload<List<DutchRailwaysStation?>>>

    @GET("v2/journey")
    fun getJourneyDetails(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("train") journeyNumber: String,
        @Query("dateTime") timestamp: String? = null
    ): Call<Payload<DutchRailwaysStops>>

    companion object {
        const val BASE_URL = "https://gateway.apiportal.ns.nl/reisinformatie-api/api/"
    }
}
