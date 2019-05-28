package nl.marc_apps.ovgo.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.marc_apps.ovgo.domainservices.PublicTransportDataRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gildor.coroutines.retrofit.await

class OVgoApiRepository(private val language: String = "en") : PublicTransportDataRepository {
    override suspend fun getDepartures(station: String) = withContext(Dispatchers.IO) {
        api.getDepartures(station, language).await()
    }

    override suspend fun getDisruptions(actual: Boolean) = withContext(Dispatchers.IO) {
        api.getDisruptions(actual, language).await()
    }

    companion object {
        private val api: OVgoApiSpecification = Retrofit.Builder()
            .baseUrl("https://ovgo.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<OVgoApiSpecification>(OVgoApiSpecification::class.java)
    }
}