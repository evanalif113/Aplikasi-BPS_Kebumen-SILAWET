package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BpsInfografisResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String
)

data class InfografisItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("date")
    val date: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("abstract")
    val abstract: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?, // Dibuat nullable, karena datanya ada yang null

    @SerializedName("link")
    val link: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)