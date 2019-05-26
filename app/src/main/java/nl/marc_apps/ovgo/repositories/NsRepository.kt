package nl.marc_apps.ovgo.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gildor.coroutines.retrofit.await

class NsRepository(private val language: String = "en") {
    suspend fun getDepartures(station: String) = withContext(Dispatchers.IO) { api.getDepartures(station, language).await() }

    suspend fun getDisruptions(actual: Boolean = true) = withContext(Dispatchers.IO) { api.getDisruptions(actual, language).await() }

    companion object {
        private val api: NsApi = Retrofit.Builder()
            .baseUrl("https://ovgo.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<NsApi>(NsApi::class.java)
    }
}