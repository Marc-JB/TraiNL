package nl.marc_apps.ovgo.data.api

import android.content.Context
import android.os.Build
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.BuildConfig
import nl.marc_apps.ovgo.utils.httpClient
import nl.marc_apps.ovgo.utils.setUserAgent
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.brotli.BrotliInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

class HttpClientImpl(val context: Context) : HttpClient {
    private inline val logger: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().also {
            it.setLevel(httpLogLevel)
        }

    override val okHttpClient by lazy {
        httpClient {
            setUserAgent(applicationUserAgent)

            addInterceptor(BrotliInterceptor)

            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(logger)
            }

            callTimeout(75.seconds.toJavaDuration())
            connectTimeout(45.seconds.toJavaDuration())
            readTimeout(30.seconds.toJavaDuration())
            writeTimeout(30.seconds.toJavaDuration())

            val cacheDir = File(context.cacheDir, NETWORK_CACHE_DIRECTORY_NAME).apply { mkdirs() }
            cache(Cache(cacheDir, maxSize = 2_000_000L))
        }
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    override val jsonConverter = json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())

    companion object {
        private val applicationUserAgent = "TraiNL/${BuildConfig.VERSION_NAME} " +
                "(Linux; Android ${Build.VERSION.RELEASE}; ${Build.MANUFACTURER} ${Build.MODEL})"

        private const val NETWORK_CACHE_DIRECTORY_NAME = "network_cache"

        private const val JSON_MEDIA_TYPE = "application/json"

        private val httpLogLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
    }
}
