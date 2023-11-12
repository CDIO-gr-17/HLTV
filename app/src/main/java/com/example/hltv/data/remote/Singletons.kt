package com.example.hltv.data.remote
import okhttp3.OkHttpClient
object OkHttpClientSingleton {
    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder().build()
    }
}
