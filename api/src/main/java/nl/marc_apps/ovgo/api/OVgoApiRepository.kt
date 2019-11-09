package nl.marc_apps.ovgo.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import nl.marc_apps.ovgo.domain.services.PublicTransportDataRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OVgoApiRepository(override var language: String = "en") :
    PublicTransportDataRepository {
    override suspend fun getDepartures(station: String) = coroutineScope {
        withContext(this.coroutineContext + Dispatchers.IO) {
            api.getDepartures(station, language).body() ?: emptyArray()
        }
    }

    override suspend fun getDisruptions(actual: Boolean) = coroutineScope {
        withContext(this.coroutineContext + Dispatchers.IO) {
            api.getDisruptions(actual, language).body() ?: emptyArray()
        }
    }

    companion object {
        private val api: OVgoApiSpecification = Retrofit.Builder()
            .baseUrl("https://ovgo.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<OVgoApiSpecification>(OVgoApiSpecification::class.java)
    }
}