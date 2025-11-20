package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BpsInfografisResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    // --- INI YANG HILANG SEBELUMNYA ---
    @SerializedName("data")
    val data: List<NewsItem> // Pastikan ini ada!
)