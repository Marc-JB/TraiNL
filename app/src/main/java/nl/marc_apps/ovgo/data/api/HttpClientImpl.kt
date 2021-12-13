package nl.marc_apps.ovgo.data.api

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import nl.marc_apps.ovgo.BuildConfig
import nl.marc_apps.ovgo.utils.dnsHttpClient
import nl.marc_apps.ovgo.utils.httpClient
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.brotli.BrotliInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@ExperimentalSerializationApi
class HttpClientImpl(val context: Context) : HttpClient {
    private inline val logger: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().also {
            it.setLevel(httpLogLevel)
        }

    override val okHttpClient by lazy {
        val bootstrapClient = httpClient {
            if (BuildConfig.DEBUG) {
                addInterceptor(logger)
            }
            addInterceptor(BrotliInterceptor)
            callTimeout(1, TimeUnit.MINUTES)
            connectTimeout(45, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            cache(Cache(context.cacheDir, MAX_CACHE_SIZE))
        }

        val dns = dnsHttpClient {
            client(bootstrapClient)
            url(CloudflareDns.RESOLVER_URL.toHttpUrl())
        }

        httpClient(bootstrapClient) {
            dns(dns)
        }
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override val jsonConverter = json.asConverterFactory(JSON_MEDIA_TYPE.toMediaType())

    companion object {
        private const val MAX_CACHE_SIZE = 10L * 1024L * 1024L

        private const val JSON_MEDIA_TYPE = "application/json"

        private val httpLogLevel: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC
    }
}
