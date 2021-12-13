package nl.marc_apps.ovgo.utils

import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.create

inline fun <reified T> retrofit(block: Retrofit.Builder.() -> Unit): T {
    return Retrofit.Builder().also(block).build().create()
}

inline fun httpClient(from: OkHttpClient? = null, block: OkHttpClient.Builder.() -> Unit): OkHttpClient {
    return (from?.newBuilder() ?: OkHttpClient.Builder()).also(block).build()
}

inline fun dnsHttpClient(block: DnsOverHttps.Builder.() -> Unit): DnsOverHttps {
    return DnsOverHttps.Builder().also(block).build()
}
