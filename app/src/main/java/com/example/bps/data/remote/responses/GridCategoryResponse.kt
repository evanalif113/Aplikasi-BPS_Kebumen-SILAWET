package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GridCategoryResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("datasets")
    val datasets: List<GridDatasetItem>
)

data class GridDatasetItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("dataset_code")
    val datasetCode: String,

    @SerializedName("dataset_name")
    val datasetName: String,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("last_update")
    val lastUpdate: String
)