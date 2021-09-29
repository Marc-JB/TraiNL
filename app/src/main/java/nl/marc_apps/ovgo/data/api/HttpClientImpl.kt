package nl.marc_apps.ovgo.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.BuildConfig
import nl.marc_apps.ovgo.utils.httpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object HttpClientImpl : HttpClient {
    private const val LOG_HTTP_REQUESTS_ON_DEBUG = true

    private const val JSON_MEDIA_TYPE = "application/json"

    private val logger = HttpLoggingInterceptor()

    init {
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    override val okHttpClient = httpClient {
        if(BuildConfig.DEBUG && LOG_HTTP_REQUESTS_ON_DEBUG) {
            addInterceptor(logger)
        }
        callTimeout(1, TimeUnit.MINUTES)
        connectTimeout(45, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override val jsonConverter = json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())
}
