package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class DatasetListResponse(
        @SerializedName("success") val success: Boolean,
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: List<DatasetResponse>,
        @SerializedName("pagination") val pagination: PaginationResponse
)

data class PaginationResponse(
        @SerializedName("current_page") val currentPage: Int,
        @SerializedName("last_page") val lastPage: Int,
        @SerializedName("per_page") val perPage: Int,
        @SerializedName("total") val total: Int
)
