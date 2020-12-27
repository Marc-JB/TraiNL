package nl.marc_apps.ovgo.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.api.models.asStation
import nl.marc_apps.ovgo.domain.models.TrainStation
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepositoryV2
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NsApiRepository(var rioApiKey: String) : PublicTransportDataRepositoryV2 {
    override suspend fun getAllStations(): List<TrainStation> {
        val response = withContext(Dispatchers.IO) {
            api.getStations(rioApiKey)
        }

        return if(response.isSuccessful) response.body()?.payload?.map { it.asStation() } ?: emptyList()
        else emptyList()
    }

    companion object {
        private val contentType = MediaType.get("application/json")

        private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        private val api: NsApiSpecification = Retrofit.Builder()
            .baseUrl("https://gateway.apiportal.ns.nl/")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(httpClient)
            .build()
            .create(NsApiSpecification::class.java)
    }
}
