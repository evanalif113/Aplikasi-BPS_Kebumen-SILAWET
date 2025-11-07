package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.data.remote.responses.TableData // <-- Pastikan import ini

// --- 🎨 WARNA-WARNA TABEL (Mirip BPS) ---
val TableBorderColor = Color(0xFFE0E0E0) // Abu-abu border
val TableHeaderBg = Color(0xFF003366) // Biru tua BPS
val TableHeaderColor = Color.White // Teks header putih

val TableRowEvenBg = Color.White // Baris genap
val TableRowOddBg = Color(0xFFF7F7F7) // Baris ganjil (abu-abu sangat muda)
// ------------------------------------------

@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
    val rows = tableData.rows

    val horizontalScrollState = rememberScrollState()

    Column(
        modifier = modifier
            // Border untuk seluruh tabel
            .border(1.dp, TableBorderColor)
    ) {

        // --- BARIS HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(horizontalScrollState)
                // Warna latar header
                .background(TableHeaderBg)
        ) {
            headers.forEachIndexed { index, header ->
                val width = if (index == 0) 180.dp else 120.dp
                val align = if (index == 0) TextAlign.Start else TextAlign.Center

                TableCell(
                    text = header,
                    isHeader = true, // Ini adalah header
                    modifier = Modifier.width(width),
                    textAlign = align
                )
            }
        }

        // --- BARIS-BARIS DATA ---
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp) // Batasi tinggi tabel
        ) {
            itemsIndexed(rows) { rowIndex, rowMap ->
                // Tentukan warna baris selang-seling
                val bgColor = if (rowIndex % 2 == 0) TableRowEvenBg else TableRowOddBg

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(horizontalScrollState) // Sinkronkan scroll
                        .background(bgColor) // Terapkan warna baris
                ) {
                    headers.forEachIndexed { colIndex, headerKey ->

                        val cellValue = rowMap[headerKey]?.toString() ?: "-"

                        val width = if (colIndex == 0) 180.dp else 120.dp
                        val align = if (colIndex == 0) TextAlign.Start else TextAlign.Center

                        TableCell(
                            text = cellValue,
                            isHeader = false, // Ini adalah data
                            modifier = Modifier.width(width),
                            textAlign = align
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable untuk satu sel
 */
@Composable
fun TableCell(
    text: String,
    isHeader: Boolean,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    // Tentukan warna teks berdasarkan apakah ini header atau bukan
    val textColor = if (isHeader) TableHeaderColor else Color.Black

    Text(
        text = text,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        fontSize = 13.sp,
        color = textColor, // Terapkan warna teks
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            // Border tipis untuk tiap sel
            .border(0.5.dp, TableBorderColor)
            .padding(horizontal = 10.dp, vertical = 12.dp) // Tambah padding
            .height(IntrinsicSize.Min)
    )
}

@Preview
@Composable
fun TabelDataSectionPreview() {
    val tableData = TableData(
        headers = listOf("Wilayah", "2021", "2022", "2023"),
        rows = listOf(
            mapOf("Wilayah" to "JAWA BARAT", "2021" to 1500, "2022" to 1600, "2023" to 1700),
            mapOf("Wilayah" to "DKI JAKARTA", "2021" to 1200, "2022" to 1300, "2023" to 1400),
            mapOf("Wilayah" to "BANTEN", "2021" to 1100, "2022" to 1150, "2023" to 1200)
        )
    )
    TabelDataSection(tableData = tableData)
}

@Preview
@Composable
fun TableCellHeaderPreview() {
    TableCell(
        text = "Header Cell",
        isHeader = true,
        modifier = Modifier.width(120.dp),
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
fun TableCellDataPreview() {
    TableCell(
        text = "Data Cell",
        isHeader = false,
        modifier = Modifier.width(120.dp),
        textAlign = TextAlign.Start
    )
}