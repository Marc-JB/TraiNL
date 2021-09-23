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
    ): Call<Set<DutchRailwaysDisruption>>

    @GET("v2/departures")
    fun getDeparturesByUicCode(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("uicCode") uicCode: String,
        @Query("lang") language: String? = null
    ): Call<Payload<Departures<Set<DutchRailwaysDeparture>>>>

    @GET("v2/arrivals")
    fun getArrivalsByUicCode(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String,
        @Query("uicCode") uicCode: String,
        @Query("lang") language: String? = null
    ): Call<Any> // TODO: Implement response type

    @GET("v2/stations")
    fun getStations(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String
    ): Call<Payload<Set<DutchRailwaysStation>>>

    companion object {
        const val BASE_URL = "https://gateway.apiportal.ns.nl/reisinformatie-api/api/"
    }
}