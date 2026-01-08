package com.example.bps.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    //private const val BASE_URL = "https://silawetbps.jeris.web.id/api/"

    private const val BASE_URL = "https://bronwyn-unmournful-distressedly.ngrok-free.dev/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
