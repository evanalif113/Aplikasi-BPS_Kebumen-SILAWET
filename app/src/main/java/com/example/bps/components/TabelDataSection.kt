package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.data.remote.responses.TableData

// --- WARNA (Tetap Sama) ---
val BpsHeaderBg = Color(0xFF1565C0)
val BpsSurface = Color.White
val BpsRowAlt = Color(0xFFF5F7FA)
val BpsBorderLine = Color(0xFFE0E0E0)

@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
    val rows = tableData.rows
    val scrollState = rememberScrollState()

    // --- PENGATURAN LEBAR KOLOM (LEBIH RAMPING) ---
    fun getColWidth(index: Int): Dp {
        return if (index == 0) {
            130.dp // Kolom Label (Wilayah/Uraian) -> Dirampingkan dari 180dp
        } else {
            75.dp  // Kolom Angka -> Dirampingkan dari 110dp (Cukup untuk 7-8 digit)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BpsSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // 1. HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BpsHeaderBg)
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                headers.forEachIndexed { index, header ->
                    TableCell(
                        text = header.uppercase(),
                        isHeader = true,
                        width = getColWidth(index),
                        // Header angka rata tengah agar terlihat seimbang di kolom sempit
                        align = if (index == 0) TextAlign.Start else TextAlign.Center
                    )

                    if (index < headers.size - 1) {
                        VerticalDivider(
                            modifier = Modifier.height(24.dp),
                            color = Color.White.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )
                    }
                }
            }

            // 2. DATA BODY
            Column {
                rows.forEachIndexed { rowIndex, rowMap ->
                    val bgColor = if (rowIndex % 2 == 0) BpsSurface else BpsRowAlt

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor)
                            .horizontalScroll(scrollState), // State scroll sama dengan header
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        headers.forEachIndexed { colIndex, headerKey ->
                            val cellValue = rowMap[headerKey]?.toString() ?: "-"

                            TableCell(
                                text = cellValue,
                                isHeader = false,
                                width = getColWidth(colIndex),
                                // Angka rata kanan agar urutan satuan/puluhan lurus
                                align = if (colIndex == 0) TextAlign.Start else TextAlign.End
                            )

                            if (colIndex < headers.size - 1) {
                                VerticalDivider(
                                    modifier = Modifier.height(40.dp),
                                    color = BpsBorderLine,
                                    thickness = 1.dp
                                )
                            }
                        }
                    }

                    if (rowIndex < rows.size - 1) {
                        HorizontalDivider(color = BpsBorderLine, thickness = 0.5.dp)
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
    width: Dp,
    align: TextAlign
) {
    Box(
        modifier = Modifier
            .width(width)
            // Padding Horizontal diperkecil (12dp -> 6dp) agar muat di kolom sempit
            .padding(horizontal = 6.dp, vertical = 10.dp),
        contentAlignment = when (align) {
            TextAlign.End -> Alignment.CenterEnd
            TextAlign.Center -> Alignment.Center
            else -> Alignment.CenterStart
        }
    ) {
        Text(
            text = text,
            color = if (isHeader) Color.White else Color(0xFF333333),
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Medium,
            // Font sedikit diperkecil agar aman
            fontSize = if (isHeader) 11.sp else 12.sp,
            textAlign = align,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun PreviewCompactTable() {
    val dummyData = TableData(
        headers = listOf("Wilayah", "2021", "2022", "2023", "Ket."),
        rows = listOf(
            mapOf("Wilayah" to "Kec. Kebumen", "2021" to "12.500", "2022" to "12.600", "2023" to "12.700", "Ket." to "Naik"),
            mapOf("Wilayah" to "Kec. Gombong", "2021" to "8.500", "2022" to "8.550", "2023" to "8.600", "Ket." to "Stabil"),
        )
    )

    Box(Modifier.padding(16.dp)) {
        TabelDataSection(tableData = dummyData)
    }
}