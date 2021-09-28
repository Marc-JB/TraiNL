package nl.marc_apps.ovgo.utils

import okhttp3.OkHttpClient
import retrofit2.*

inline fun <reified T> retrofit(block: Retrofit.Builder.() -> Unit): T {
    return Retrofit.Builder().also(block).build().create()
}

inline fun httpClient(block: OkHttpClient.Builder.() -> Unit): OkHttpClient {
    return OkHttpClient.Builder().also(block).build()
}
