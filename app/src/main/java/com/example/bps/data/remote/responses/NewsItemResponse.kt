package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale // Jangan lupa import ini

data class NewsItemResponse(
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

    @SerializedName("image_url")
    val imageUrl: String? = null,

    // --- PENANGANAN RINGKASAN GANDA ---
    @SerializedName("abstract")
    val abstract: String? = null,

    @SerializedName("description")
    val description: String? = null,

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
        // 1. Ambil 10 karakter pertama (format yyyy-MM-dd)
        val rawDate = if (date.length >= 10) date.take(10) else date

        return try {
            // 2. Parsing: Ubah String menjadi Objek Tanggal
            val parsedDate = LocalDate.parse(rawDate)

            // 3. Buat Formatter dengan Locale Indonesia
            // 'dd'   = Tanggal 2 digit (01, 15, 30)
            // 'MMMM' = Nama Bulan Lengkap (Januari, Februari)
            // 'yyyy' = Tahun (2026)
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id", "ID"))

            // 4. Format tanggalnya
            parsedDate.format(formatter) // Output: "01 Januari 2026"

        } catch (e: Exception) {
            // Jika gagal parsing, kembalikan teks aslinya
            rawDate
        }
    }
}