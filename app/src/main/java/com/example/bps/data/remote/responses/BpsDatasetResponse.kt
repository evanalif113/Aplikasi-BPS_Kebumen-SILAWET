package com.example.bps.data.remote.responses
/**
 * Ini adalah data class utama yang membungkus seluruh respons JSON.
 */
data class BpsDatasetResponse(
    val dataset: DatasetInfo,
    val table: TableData,
    val chart: ChartData,
    val insights: List<Insight>
)

/**
 * Mewakili objek "dataset" di dalam JSON.
 * Berisi metadata tabel.
 */
data class DatasetInfo(
    val id: Int,
    val dataset_code: String,
    val dataset_name: String,
    val insight_type: String,
    val subject: String,
    val category: Int,
    val source: String,
    val last_update: String,
    val created_at: String,
    val updated_at: String
)

/**
 * Mewakili objek "table" di dalam JSON.
 */
data class TableData(
    val headers: List<String>,

    // jadi kita gunakan Map<String, Any> untuk menangkap key-value secara dinamis.
    val rows: List<Map<String, Any>>
)

/**
 * Mewakili objek "chart" di dalam JSON.
 */
data class ChartData(
    val type: String,
    val title: String,
    val labels: List<String>,

    // Dibuat nullable (?), karena chart "pyramid" tidak punya "data",
    // tapi chart "pie" dan "bar" punya.
    val data: List<Number>? = null,

    // Dibuat nullable (?), karena hanya chart "pyramid" yang punya "datasets".
    val datasets: List<ChartDataset>? = null
)

/**
 * Bagian dari ChartData, khusus untuk tipe chart yang punya
 * beberapa set data (seperti 'pyramid').
 */
data class ChartDataset(
    val label: String,
    val data: List<Number>
)

/**
 * Mewakili satu objek di dalam array "insights".
 */
data class Insight(
    val title: String,
    val value: String,
    val description: String
)