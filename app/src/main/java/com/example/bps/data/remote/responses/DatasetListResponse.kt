package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class DatasetListResponse(
        @SerializedName("success") val success: Boolean,
        @SerializedName("data") val pagination: DatasetPaginationResponse
)

data class DatasetPaginationResponse(
        @SerializedName("current_page") val currentPage: Int,
        @SerializedName("data") val datasets: List<DatasetResponse>,
        @SerializedName("last_page") val lastPage: Int,
        @SerializedName("per_page") val perPage: Int,
        @SerializedName("total") val total: Int
)

