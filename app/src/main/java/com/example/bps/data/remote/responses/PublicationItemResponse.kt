package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PublicationItemResponse(
    val id: Int,
    val title: String,

    @SerializedName("release_date") // Tanggal Rilis
    val releaseDate: String,

    @SerializedName("cover_url")    // URL untuk cover
    val coverUrl: String,

    @SerializedName("pdf_url")      // URL untuk Unduh PDF
    val pdfUrl: String,

    val abstract: String? = null
) {
    // Fungsi Helper Tanggal
    fun getSimpleDate(): String {
        return if (releaseDate.length >= 10) releaseDate.substring(0, 10) else releaseDate
    }
}