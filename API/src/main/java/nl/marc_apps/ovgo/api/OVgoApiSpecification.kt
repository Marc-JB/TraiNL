package nl.marc_apps.ovgo.api

import nl.marc_apps.ovgo.domainmodels.Departure
import nl.marc_apps.ovgo.domainmodels.Disruption
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface OVgoApiSpecification {
    @GET("stations/{id}/departures.json")
    suspend fun getDepartures(
        @Path("id") station: String,
        @Header("accept-language") language: String = "en"
    ): Response<Array<Departure>>

    @GET("disruptions.json")
    suspend fun getDisruptions(
        @Query("actual") actual: Boolean = true,
        @Header("accept-language") language: String = "en"
    ): Response<Array<Disruption>>
}