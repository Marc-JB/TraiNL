package nl.marc_apps.ovgo.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OVgoApiRepository(override var language: String = "en") : PublicTransportDataRepository {
    override suspend fun getDepartures(station: String): Set<Departure> {
        return withContext(Dispatchers.IO) {
            api.getDepartures(station, language).body() ?: emptySet()
        }
    }

    override suspend fun getDisruptions(actual: Boolean): Set<Disruption> {
        return withContext(Dispatchers.IO) {
            api.getDisruptions(actual, language).body() ?: emptySet()
        }
    }

    companion object {
        private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(45, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        private val api: OVgoApiSpecification = Retrofit.Builder()
            .baseUrl("https://ovgo.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(OVgoApiSpecification::class.java)
    }
}