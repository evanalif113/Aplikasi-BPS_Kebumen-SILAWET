package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class DatasetListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("count")
    val count: Int,

    @SerializedName("data")
    val data: List<DatasetResponse> // Ini list data yang sebenarnya
)