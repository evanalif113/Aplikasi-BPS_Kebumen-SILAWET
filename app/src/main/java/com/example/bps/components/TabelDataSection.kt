package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.bps.data.remote.responses.TableData

// --- KONFIGURASI WARNA KHAS BPS ---
val BpsBlue = Color(0xFF0D47A1)      // Biru Tua BPS (Header)
val BpsBorder = Color(0xFFE0E0E0)    // Abu Border Tipis
val RowOdd = Color(0xFFF9FAFB)       // Abu Sangat Muda (Baris Ganjil)
val RowEven = Color.White            // Putih (Baris Genap)
val TextHeader = Color.White         // Teks Header Putih
val TextBody = Color(0xFF333333)     // Teks Isi Hitam Abu

@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
//    val headers = tableData.headers.filter { it != "Tahun" }


    val rows = tableData.rows

    // State untuk sinkronisasi scroll horizontal
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BpsBorder)
    ) {
        // --- 1. HEADER TABEL (Biru BPS) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BpsBlue) // <--- Warna Biru BPS
                .horizontalScroll(scrollState) // Scroll horizontal
        ) {
            headers.forEachIndexed { index, header ->
                // Kolom pertama (Label) lebih lebar, Kolom angka lebih kecil
                val width = if (index == 0) 160.dp else 100.dp
                val align = if (index == 0) TextAlign.Start else TextAlign.End

                TableCell(
                    text = header,
                    isHeader = true,
                    width = width,
                    align = align
                )
            }
        }

        // --- 2. ISI TABEL ---
        // PENTING: Gunakan Column biasa, bukan LazyColumn
        // Karena parent-nya (DatasetDetailScreen) sudah pakai LazyColumn.
        Column {
            rows.forEachIndexed { rowIndex, rowMap ->
                val bgColor = if (rowIndex % 2 == 0) RowEven else RowOdd

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .horizontalScroll(scrollState) // Ikut scroll header
                ) {
                    headers.forEachIndexed { colIndex, headerKey ->
                        val cellValue = rowMap[headerKey]?.toString() ?: "-"

                        // Lebar harus SAMA PERSIS dengan Header
                        val width = if (colIndex == 0) 160.dp else 100.dp
                        val align = if (colIndex == 0) TextAlign.Start else TextAlign.End

                        TableCell(
                            text = cellValue,
                            isHeader = false,
                            width = width,
                            align = align
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    isHeader: Boolean,
    width: androidx.compose.ui.unit.Dp,
    align: TextAlign
) {
    Text(
        text = text,
        color = if (isHeader) TextHeader else TextBody,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        fontSize = 13.sp,
        textAlign = align,
        modifier = Modifier
            .width(width) // Lebar fix agar sejajar
            .padding(12.dp), // Padding biar lega
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
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

//@Preview
//@Composable
//fun TableCellHeaderPreview() {
//    TableCell(
//        text = "Header Cell",
//        isHeader = true,
//        modifier = Modifier.width(120.dp),
//        textAlign = TextAlign.Center
//    )
//}
//
//@Preview
//@Composable
//fun TableCellDataPreview() {
//    TableCell(
//        text = "Data Cell",
//        isHeader = false,
//        modifier = Modifier.width(120.dp),
//        textAlign = TextAlign.Start
//    )
//}