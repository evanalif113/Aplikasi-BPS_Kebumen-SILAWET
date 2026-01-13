package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    val abstract: String? = "Tidak ada Abstrak"
) {
    /**
     * Mengambil tanggal saja (YYYY-MM-DD) dari string waktu yang panjang.
     * Contoh input: "2023-12-01 15:30:00" -> Output: "2023-12-01"
     */
    fun getSimpleDate(): String {
        // 1. Ambil 10 karakter pertama (format yyyy-MM-dd)
        val rawDate = if (releaseDate.length >= 10) releaseDate.take(10) else releaseDate

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