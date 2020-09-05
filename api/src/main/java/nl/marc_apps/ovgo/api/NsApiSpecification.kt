package nl.marc_apps.ovgo.api

import nl.marc_apps.ovgo.api.models.NsResponse
import nl.marc_apps.ovgo.api.models.NsStation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NsApiSpecification {
    @GET("/reisinformatie-api/api/v2/stations")
    suspend fun getStations(
        @Header("Ocp-Apim-Subscription-Key") apiKey: String
    ): Response<NsResponse<Collection<NsStation>>>
}
