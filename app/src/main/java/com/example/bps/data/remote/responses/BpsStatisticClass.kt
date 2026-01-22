package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

/**
 * 1. RESPONSE UTAMA (Root)
 */
data class DatasetDetailResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: DatasetDetailData? // Bisa null jika request gagal
)

/**
 * 2. DATA HOLDER (Dulu ini adalah "DatasetClass")
 */
data class DatasetDetailData(
    @SerializedName("dataset")
    val dataset: DatasetMetadata,

    @SerializedName("unit")
    val unit: String?,

    @SerializedName("available_years")
    val availableYears: List<Int> = emptyList(),

    @SerializedName("current_year")
    val currentYear: Int,

    @SerializedName("table")
    val table: TableData,

    @SerializedName("chart")
    val chart: ChartData,

    @SerializedName("insights")
    val insights: List<Insight> = emptyList()
)

/**
 * Metadata Dataset
 */
data class DatasetMetadata(
    @SerializedName("id")
    val id: Int,

    @SerializedName("dataset_code")
    val datasetCode: String,

    @SerializedName("dataset_name")
    val datasetName: String,

    @SerializedName("insight_type")
    val insightType: String,

    @SerializedName("subject")
    val subject: String,

    @SerializedName("category")
    val category: Int,

    @SerializedName("source")
    val source: String,

    @SerializedName("last_update")
    val lastUpdate: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * Data Tabel
 */
data class TableData(
    @SerializedName("headers")
    val headers: List<String>,

    // Rows dinamis (Map key-value)
    @SerializedName("rows")
    val rows: List<Map<String, Any>>
)

/**
 * Data Chart
 */
data class ChartData(
    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("labels")
    val labels: List<String>,

    @SerializedName("data")
    val data: List<Number>? = null,

    @SerializedName("datasets")
    val datasets: List<ChartDataset>? = null
)

/**
 * Dataset untuk Chart Multi-Series (cth: Pyramid)
 */
data class ChartDataset(
    @SerializedName("label")
    val label: String,

    @SerializedName("data")
    val data: List<Number>
)

/**
 * Insight / Kesimpulan Otomatis
 */
data class Insight(
    @SerializedName("title")
    val title: String,

    @SerializedName("value")
    val value: String,

    @SerializedName("description")
    val description: String
)