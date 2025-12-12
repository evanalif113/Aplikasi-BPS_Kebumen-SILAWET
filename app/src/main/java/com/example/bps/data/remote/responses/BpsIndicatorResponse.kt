package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

// 1. Wadah Utama (Sesuai struktur root JSON)
data class BpsIndicatorResponse(
    val status: String,
    val message: String,
    val data: List<IndicatorItem>, // Array data indikator
    @SerializedName("total_indicators")
    val totalIndicators: Int,
    val timestamp: String
)

// 2. Wadah Item Indikator (Sesuai objek di dalam array "data")
data class IndicatorItem(
    val slug: String,
    @SerializedName("category_title")
    val categoryTitle: String,
    @SerializedName("display_name")
    val displayName: String?, // Bisa null
    @SerializedName("dataset_id")
    val datasetId: Int,
    @SerializedName("dataset_code")
    val datasetCode: String,
    @SerializedName("dataset_name")
    val datasetName: String,
    val value: Any, // Gunakan Any karena bisa berupa Integer (1414754) atau Double (5.07)
    val year: Int,
    val unit: String
) {
    // --- Helper Function: Untuk Mengambil Label Judul ---
    // Mengutamakan display_name, jika kosong pakai category_title
    fun getLabel(): String {
        return if (!displayName.isNullOrBlank()) displayName else categoryTitle
    }

    // --- Helper Function: Format Nilai Angka ---
    // Mengubah 1414754 menjadi "1,41" (jika jutaan) atau membiarkan "5.07"
    fun getFormattedValue(): String {
        return try {
            val doubleVal = value.toString().toDouble()
            when {
                doubleVal >= 1_000_000 -> String.format("%.2f", doubleVal / 1_000_000) // 1.41
                else -> value.toString() // 5.07 atau 71.93
            }
        } catch (e: Exception) {
            value.toString()
        }
    }

    // --- Helper Function: Format Satuan ---
    // Menambahkan "Juta" jika angkanya jutaan
    fun getFormattedUnit(): String {
        return try {
            val doubleVal = value.toString().toDouble()
            if (doubleVal >= 1_000_000) "Juta $unit" else unit
        } catch (e: Exception) {
            unit
        }
    }
}