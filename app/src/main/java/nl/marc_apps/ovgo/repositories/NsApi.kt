package nl.marc_apps.ovgo.repositories

import nl.marc_apps.ovgo.models.Departure
import nl.marc_apps.ovgo.models.Disruption
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface NsApi {
    @GET("stations/{id}/departures.json")
    fun getDepartures(
        @Path("id") station: String,
        @Header("accept-language") language: String = "en"
    ): Call<Array<Departure>>

    @GET("disruptions.json")
    fun getDisruptions(
        @Query("actual") actual: Boolean = true,
        @Header("accept-language") language: String = "en"
    ): Call<Array<Disruption>>
}