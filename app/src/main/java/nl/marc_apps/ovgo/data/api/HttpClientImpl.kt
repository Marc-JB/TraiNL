package nl.marc_apps.ovgo.data.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.BuildConfig
import nl.marc_apps.ovgo.utils.httpClient
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
class HttpClientImpl(val context: Context) : HttpClient {
    private val logger by lazy {
        HttpLoggingInterceptor().also {
            it.setLevel(if (LOG_FULL_HTTP_REQUESTS_ON_DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            })
        }
    }

    override val okHttpClient = httpClient {
        if (BuildConfig.DEBUG) {
            addInterceptor(logger)
        }
        callTimeout(1, TimeUnit.MINUTES)
        connectTimeout(45, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
        cache(Cache(context.cacheDir, 10L * 1024L * 1024L))
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override val jsonConverter = json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())

    companion object {
        private const val LOG_FULL_HTTP_REQUESTS_ON_DEBUG = false

        private const val JSON_MEDIA_TYPE = "application/json"
    }
}
