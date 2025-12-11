package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class PublicationItemResponse(
    val id: Int,
    val title: String,

    @SerializedName("release_date")
    val releaseDate: String, // UBAH KE String (dan Nullable biar aman)

    @SerializedName("cover_url")
    val coverUrl: String,

    @SerializedName("pdf_url")
    val pdfUrl: String,

    @SerializedName("abstract") // Tambahkan anotasi ini agar konsisten
    val abstract: String? = null
) {
    /**
     * Mengambil tanggal saja (YYYY-MM-DD) dari string waktu yang panjang.
     * Contoh input: "2023-12-01 15:30:00" -> Output: "2023-12-01"
     */
    fun getSimpleDate(): String {
        val safeDate = releaseDate ?: return "-" // Handle jika null

        return if (safeDate.length >= 10) {
            safeDate.substring(0, 10)
        } else {
            safeDate
        }
    }
}