package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BpsIndicatorResponse(
    val status: String,
    val message: String,
    val data: List<IndicatorItem>,
)

data class IndicatorItem(
    val slug: String,
    @SerializedName("category_title")
    val categoryTitle: String,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("dataset_id")
    val datasetId: Int,
    @SerializedName("dataset_code")
    val datasetCode: String,
    @SerializedName("dataset_name")
    val datasetName: String,

    // --- PERUBAHAN DI SINI: SEMUA JADI NULLABLE (?) ---
    val value: Any?,   // Bisa null
    val year: Int?,    // Bisa null
    val unit: String?  // Bisa null
) {
    fun getLabel(): String {
        return if (!displayName.isNullOrBlank()) displayName else categoryTitle
    }

    // Format Nilai (Handle Value Null)
    fun getFormattedValue(): String {
        if (value == null) return "-" // Jika value kosong, tampilkan strip

        return try {
            val doubleVal = value.toString().toDouble()
            when {
                doubleVal >= 1_000_000 -> String.format("%.2f", doubleVal / 1_000_000)
                else -> value.toString()
            }
        } catch (e: Exception) {
            value.toString()
        }
    }

    // Format Satuan (Handle Unit & Value Null)
    fun getFormattedUnit(): String {
        // Ambil unit, jika null ganti jadi string kosong ""
        val safeUnit = unit ?: ""

        // Jika value null, kita tidak bisa memproses juta-jutaan, kembalikan unit saja
        if (value == null) return safeUnit

        return try {
            val doubleVal = value.toString().toDouble()
            if (doubleVal >= 1_000_000) "Juta $safeUnit" else safeUnit
        } catch (e: Exception) {
            safeUnit
        }
    }
}