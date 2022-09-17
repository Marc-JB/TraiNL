package nl.marc_apps.ovgo.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.create

inline fun <reified T> retrofit(block: Retrofit.Builder.() -> Unit): T {
    return Retrofit.Builder().apply(block).build().create()
}

inline fun httpClient(block: OkHttpClient.Builder.() -> Unit): OkHttpClient {
    return OkHttpClient.Builder().apply(block).build()
}

inline fun Request.builder(block: Request.Builder.() -> Unit): Request {
    return newBuilder().apply(block).build()
}

fun OkHttpClient.Builder.setUserAgent(userAgent: String) {
    addNetworkInterceptor { chain ->
        chain.proceed(
            chain.request().builder {
                header("User-Agent", userAgent)
            }
        )
    }
}
