package com.example.bps.components

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import com.example.bps.data.remote.responses.ChartData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.example.bps.data.remote.responses.ChartDataset
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.PercentFormatter


// Warna BPS
const val BPS_BLUE_HEX = "#0D47A1"
const val BPS_ORANGE_HEX = "#FF9800"

@Composable
fun ChartSection(
    chartData: ChartData,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 350.dp, max = 600.dp)
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Judul Grafik
            Text(
                text = chartData.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Area Grafik
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when (chartData.type) {
                    "line" -> RenderLineChart(chartData)
                    "bar" -> RenderBarChart(chartData)
                    "pie" -> RenderPieChart(chartData)
                    else -> RenderBarChart(chartData)
                }
            }
        }
    }
}

@Preview
@Composable
fun ChartSectionPreview() {
    val chartData = ChartData(
        type = "bar",
        title = "Sample Bar Chart",
        labels = listOf("2020", "2021", "2022"),
        datasets = listOf(
            ChartDataset(
                label = "Data",
                data = listOf(10.0, 20.0, 15.0)
            )
        )
    )
    ChartSection(chartData)
}
// ==========================================
// 1. RENDER LINE CHART (Untuk Tren/Time Series)
// ==========================================
@Composable
fun RenderLineChart(data: ChartData) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                // Konfigurasi Dasar
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                // Sumbu X (Bawah/Tahun)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textColor = AndroidColor.DKGRAY
                }

                // Sumbu Y (Kiri)
                axisLeft.apply {
                    textColor = AndroidColor.DKGRAY
                    setDrawGridLines(true)
                }
                axisRight.isEnabled = false // Hilangkan angka di kanan

                // Animasi
                animateX(1000)
            }
        },
        update = { chart ->
            // Ambil data dari API
            // API BPS Anda mengembalikan 'datasets' array, kita ambil yang pertama
            val apiDataset = data.datasets?.firstOrNull()
            val rawValues = apiDataset?.data ?: emptyList() // List<Double>

            if (rawValues.isNotEmpty()) {
                // Konversi Double ke Entry(x, y)
                val entries = rawValues.mapIndexed { index, value ->
                    Entry(index.toFloat(), value.toFloat())
                }

                val lineDataSet = LineDataSet(entries, "Data").apply {
                    color = AndroidColor.parseColor(BPS_BLUE_HEX)
                    setCircleColor(AndroidColor.parseColor(BPS_ORANGE_HEX))
                    lineWidth = 3f
                    circleRadius = 4f
                    setDrawValues(true) // Tampilkan angka di titik
                    valueTextSize = 10f
                    mode = LineDataSet.Mode.LINEAR

                    // Efek Arsir Bawah (Area)
                    setDrawFilled(true)
                    fillColor = AndroidColor.parseColor(BPS_BLUE_HEX)
                    fillAlpha = 30
                }

                // Masukkan data ke chart
                chart.data = LineData(lineDataSet)

                // Pasang Label Sumbu X (2020, 2021, dst)
                // Pastikan label dikonversi ke String
                val labels = data.labels.map { it }
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                // Refresh agar muncul
                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        }
    )
}

@Preview
@Composable
fun RenderLineChartPreview() {
    val data = ChartData(
        type = "line",
        title = "Line Chart Preview",
        labels = listOf("2020", "2021", "2022", "2023"),
        datasets = listOf(
            ChartDataset(
                label = "Data",
                data = listOf(120.0, 150.0, 130.0, 180.0)
            )
        )
    )
    RenderLineChart(data)
}
// ==========================================
// 2. RENDER BAR CHART (Untuk Kategori)
// ==========================================
@Composable
fun RenderBarChart(data: ChartData) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    labelRotationAngle = -45f // Miringkan label biar gak tabrakan
                }

                axisLeft.setDrawGridLines(true)
                axisRight.isEnabled = false

                animateY(1000)
            }
        },
        update = { chart ->
            // Logika ambil data (handle format array 'data' atau 'datasets')
            val apiDataset = data.datasets?.firstOrNull()
            val rawValues = apiDataset?.data ?: data.data ?: emptyList()

            if (rawValues.isNotEmpty()) {
                val entries = rawValues.mapIndexed { index, value ->
                    BarEntry(index.toFloat(), value.toFloat())
                }

                val barDataSet = BarDataSet(entries, "Data").apply {
                    color = AndroidColor.parseColor(BPS_BLUE_HEX)
                    valueTextSize = 10f
                }

                chart.data = BarData(barDataSet)

                // Label Sumbu X
                val labels = data.labels.map { it }
                chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                chart.notifyDataSetChanged()
                chart.invalidate()
            }
        }
    )
}

@Preview
@Composable
fun RenderBarChartPreview() {
    val data = ChartData(
        type = "bar",
        title = "Bar Chart Preview",
        labels = listOf("Category A", "Category B", "Category C"),
        datasets = listOf(
            ChartDataset(
                label = "Data",
                data = listOf(50.0, 80.0, 65.0)
            )
        )
    )
    RenderBarChart(data)
}
@Composable
fun RenderPieChart(data: ChartData) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false

                // Konfigurasi Pie
                isDrawHoleEnabled = true       // Bolong tengah (Donut style)
                setHoleColor(AndroidColor.WHITE)
                setTransparentCircleAlpha(110)
                holeRadius = 50f               // Besar lubang tengah
                transparentCircleRadius = 55f

                setUsePercentValues(true)      // Tampilkan angka %
                setEntryLabelColor(AndroidColor.BLACK) // Warna label kategori di dalam pie
                setEntryLabelTextSize(10f)

                // Legenda
                legend.isEnabled = true
                legend.isWordWrapEnabled = true // Agar legenda turun ke bawah kalau panjang

                animateY(1400, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
            }
        },
        update = { chart ->
            val apiDataset = data.datasets?.firstOrNull()
            val rawValues = apiDataset?.data ?: data.data ?: emptyList()

            if (rawValues.isNotEmpty()) {
                val entries = rawValues.mapIndexed { index, value ->
                    // Ambil label kategori dari data.labels
                    val label = data.labels.getOrNull(index) ?: ""
                    PieEntry(value.toFloat(), label)
                }

                val dataSet = PieDataSet(entries, "").apply {
                    sliceSpace = 3f // Jarak antar potongan
                    selectionShift = 5f

                    // Warna-warni Pie
                    // Kita pakai template warna pastel bawaan library biar cantik
                    colors = ColorTemplate.MATERIAL_COLORS.toList() + ColorTemplate.JOYFUL_COLORS.toList()

                    // Tampilkan nilai di luar pie agar rapi jika potongannya kecil
                    yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
                    valueLinePart1OffsetPercentage = 80f
                    valueLinePart1Length = 0.2f
                    valueLinePart2Length = 0.4f
                    valueLineWidth = 1f
                    valueLineColor = AndroidColor.DKGRAY
                }

                val pieData = PieData(dataSet).apply {
                    setValueFormatter(PercentFormatter(chart)) // Format ada % nya
                    setValueTextSize(11f)
                    setValueTextColor(AndroidColor.BLACK)
                }

                chart.data = pieData
                chart.highlightValues(null) // Hapus highlight
                chart.invalidate()
            }
        }
    )
}

@Preview
@Composable
fun RenderPieChartPreview() {
    val data = ChartData(
        type = "pie",
        title = "Pie Chart Preview",
        labels = listOf("Red", "Blue", "Green"),
        data = listOf(30.0, 50.0, 20.0),
        datasets = listOf(
            ChartDataset(label = "Data", data = listOf(30.0, 50.0, 20.0))
        )
    )
    RenderPieChart(data)
}