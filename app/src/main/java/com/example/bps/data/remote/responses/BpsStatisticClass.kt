package com.example.bps.data.remote.responses
/**
 * Ini adalah data class utama yang membungkus seluruh respons JSON.
 */
data class BpsDatasetClass(
    val dataset: DatasetInfo,
    val available_years: List<Int> = emptyList(),
    val current_year: Int,
    val table: TableData,
    val chart: ChartData,
    val insights: List<Insight> = emptyList()
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
    val data: List<Number>? = null,
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