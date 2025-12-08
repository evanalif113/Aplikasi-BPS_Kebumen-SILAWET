package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class SimpleDatasetListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("data")
    val data: List<SimpleDatasetResponse> // Ini list data yang sebenarnya
)