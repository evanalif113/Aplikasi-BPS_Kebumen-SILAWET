package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

// INI ADALAH ISI YANG BENAR UNTUK BpsNewsResponse.kt
data class BpsNewsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<NewsItem> // <-- Dia memanggil NewsItem yang ada di file sebelah
)