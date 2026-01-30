package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class DatasetResponse(
    val id: Int,

    @SerializedName("dataset_name")
    val dataset_name: String,

    val subject: String,

    @SerializedName("updated_at")
    val updated_at: String? // Biarkan ini menerima string mentah dari API
) {
    // --- TAMBAHKAN INI ---
    // Property ini akan otomatis memformat tanggal saat dipanggil
    val tanggalAngka: String
        get() {
            if (updated_at.isNullOrEmpty()) return "-"

            return try {
                // 1. Parse dari format ISO API
                val parsedDate = ZonedDateTime.parse(updated_at)

                // 2. Ubah ke format angka standar Indonesia (dd-MM-yyyy)
                // Contoh output: 13-01-2026
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale("id", "ID"))

                parsedDate.format(formatter)
            } catch (e: Exception) {
                // Jika gagal parsing (misal format beda), kembalikan aslinya atau "-"
                updated_at.take(10) // Fallback ambil 10 digit pertama saja
            }
        }

    // Opsional: Jika ingin format Jam juga (13-01-2026 14:30)
    val tanggalJamAngka: String
        get() {
            if (updated_at.isNullOrEmpty()) return "-"
            return try {
                val parsedDate = ZonedDateTime.parse(updated_at)
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale("id", "ID"))
                parsedDate.format(formatter)
            } catch (e: Exception) { "-" }
        }
}