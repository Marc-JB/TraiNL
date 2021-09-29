package nl.marc_apps.ovgo.data.api

import okhttp3.OkHttpClient
import retrofit2.Converter

interface HttpClient {
    val okHttpClient: OkHttpClient

    val jsonConverter: Converter.Factory
}
