package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PublicationItem(
    val id: Int,
    val title: String,

    // Sesuai JSON: "release_date": "2025-10-29T17:00:00..."
    @SerializedName("release_date")
    val releaseDate: String,

    // Sesuai JSON: "cover_url"
    @SerializedName("cover_url")
    val coverUrl: String,

    // Sesuai JSON: "pdf_url"
    @SerializedName("pdf_url")
    val pdfUrl: String,

    // Sesuai JSON: "abstract"
    val abstract: String? = null


)
{
    // --- PASTIKAN FUNGSI INI JUGA ADA DI SINI ---
    fun getSimpleDate(): String {
        return if (releaseDate.length >= 10) releaseDate.substring(0, 10) else releaseDate
    }
}