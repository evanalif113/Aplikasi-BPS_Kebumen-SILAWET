package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GridMenuResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<GridMenuItem>
)

data class GridMenuItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("dataset_count")
    val datasetCount: Int
)