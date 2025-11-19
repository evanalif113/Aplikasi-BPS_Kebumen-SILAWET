package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

// INI ADALAH ISI YANG BENAR UNTUK NewsItem.kt
data class NewsItem(

    @SerializedName("id")

    val id: Int,


    @SerializedName("title")

    val title: String,


    @SerializedName("date")

    val date: String,


    @SerializedName("category")

    val category: String? = null,


    // --- PENANGANAN GAMBAR GANDA ---

    // API Berita pakai ini

    @SerializedName("thumbnail_url")

    val thumbnailUrl: String? = null,


    // API Infografis pakai ini (Inilah yang kurang di kode lama Anda)

    @SerializedName("image_url")

    val imageUrl: String? = null,


    // --- PENANGANAN RINGKASAN GANDA ---

    @SerializedName("abstract")

    val abstract: String? = null,


    @SerializedName("description")

    val description: String? = null,


    // --- KHUSUS BRS (Berita Resmi Statistik) ---

    @SerializedName("pdf_url")

    val pdfUrl: String? = null,


    @SerializedName("link")

    val link: String? = null

) {

    // FUNGSI SAKTI: Otomatis pilih gambar yang ada

    fun getDisplayImage(): String {

        return thumbnailUrl ?: imageUrl ?: ""

    }



    fun getSummary(): String {

        return abstract ?: description ?: ""

    }


    fun getSimpleDate(): String {

        return if (date.length >= 10) date.substring(0, 10) else date

    }

}