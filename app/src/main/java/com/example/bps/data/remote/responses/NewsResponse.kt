package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

/**
 * Ini adalah data class utama untuk respons News.
 */
data class BpsNewsResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<NewsItem>,

    @SerializedName("pagination")
    val pagination: Pagination
)

/**
 * Mewakili satu item berita di dalam array "data".
 */
data class NewsItem(
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

/**
 * Mewakili objek "pagination" di dalam respons News.
 */
data class Pagination(
    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("total")
    val total: Int
)