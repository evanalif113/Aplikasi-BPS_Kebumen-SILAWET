package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PublicationItem(
    val id: Int,
    val title: String,

    @SerializedName("release_date") // Beda dengan NewsItem (date)
    val releaseDate: String,

    @SerializedName("cover_url")    // Beda dengan NewsItem (thumbnail_url)
    val coverUrl: String,

    @SerializedName("pdf_url")      // Khas buku
    val pdfUrl: String,

    val abstract: String? = null
) {
    // Fungsi Helper Tanggal
    fun getSimpleDate(): String {
        return if (releaseDate.length >= 10) releaseDate.substring(0, 10) else releaseDate
    }
}