package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

data class BpsIndicatorResponse(

        @SerializedName("success") 
        val success: Boolean,
        val message: String?,

        @SerializedName("data") 
        val data: List<IndicatorItem>,

        @SerializedName("meta") val meta: IndicatorMeta?
)

data class IndicatorMeta(
        @SerializedName("total_indicators") val totalIndicators: Int?,
        @SerializedName("timestamp") val timestamp: String?
)

data class IndicatorItem(
        val slug: String,
        @SerializedName("category_title") val categoryTitle: String,
        @SerializedName("display_name") val displayName: String?,
        @SerializedName("dataset_id") val datasetId: Int,
        @SerializedName("dataset_code") val datasetCode: String,
        @SerializedName("dataset_name") val datasetName: String,
        val value: Any?,
        val year: Int?,
        val unit: String?
) {
    fun getLabel(): String {
        return if (!displayName.isNullOrBlank()) displayName else categoryTitle
    }

    fun getFormattedValue(): String {
        if (value == null) return "-"

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

    fun getFormattedUnit(): String {
        val safeUnit = unit ?: ""
        if (value == null) return safeUnit

        return try {
            val doubleVal = value.toString().toDouble()
            if (doubleVal >= 1_000_000) "Juta $safeUnit" else safeUnit
        } catch (e: Exception) {
            safeUnit
        }
    }
}