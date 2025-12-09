package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BpsNewsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<NewsItemResponse>
)