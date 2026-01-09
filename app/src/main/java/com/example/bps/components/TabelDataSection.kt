package com.example.bps.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.data.remote.responses.TableData

// --- WARNA ---
val BpsHeaderBg = Color(0xFF1565C0)
val BpsSurface = Color.White
val BpsRowAlt = Color(0xFFF5F7FA)
val BpsBorderLine = Color(0xFFE0E0E0)

@OptIn(ExperimentalFoundationApi::class) // Diperlukan untuk stickyHeader
@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
    val rows = tableData.rows

    // Scroll state untuk Horizontal (Kanan-Kiri)
    val horizontalScrollState = rememberScrollState()
    val density = LocalDensity.current

    // State Lebar Kolom
    val columnWidths = remember { mutableStateMapOf<Int, Dp>() }

    // --- LOGIKA AUTO-FIT WIDTH ---
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidth = maxWidth

        LaunchedEffect(headers, availableWidth) {
            val baseWidths = headers.mapIndexed { index, _ ->
                if (index == 0) 140.dp else 80.dp // Lebar base sedikit dibesarkan
            }
            val totalBaseWidth = baseWidths.sumOf { it.value.toDouble() }.dp

            if (totalBaseWidth < availableWidth) {
                val extraSpace = availableWidth - totalBaseWidth
                val extraPerCol = extraSpace / headers.size
                headers.forEachIndexed { index, _ ->
                    if (!columnWidths.containsKey(index)) {
                        columnWidths[index] = baseWidths[index] + extraPerCol
                    }
                }
            } else {
                headers.forEachIndexed { index, _ ->
                    if (!columnWidths.containsKey(index)) {
                        columnWidths[index] = baseWidths[index]
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp) // PENTING: Batasi tinggi LazyColumn agar bisa di-scroll di dalam layar kecil
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = BpsSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            // Container utama yang bisa di-scroll secara HORIZONTAL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState)
            ) {
                // Gunakan LazyColumn untuk performa vertikal yang lebih baik (ribuan data aman)
                LazyColumn(
                    modifier = Modifier.width(
                        // Hitung total lebar tabel agar LazyColumn tahu batas kanannya
                        columnWidths.values.sumOf { it.value.toDouble() }.dp.coerceAtLeast(availableWidth)
                    )
                ) {
                    // 1. STICKY HEADER
                    // Header akan menempel di atas saat di-scroll ke bawah
                    stickyHeader {
                        Row(
                            modifier = Modifier.background(BpsHeaderBg),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            headers.forEachIndexed { index, header ->
                                val currentWidth = columnWidths[index] ?: 80.dp
                                val cleanHeader = header.removeSuffix(".0")

                                TableCell(
                                    text = cleanHeader.uppercase(),
                                    isHeader = true,
                                    width = currentWidth,
                                    align = if (index == 0) TextAlign.Start else TextAlign.Center
                                )

                                // Resizer
                                if (index < headers.size - 1) {
                                    DraggableDivider(
                                        onResize = { dragAmountPx ->
                                            val deltaDp = with(density) { dragAmountPx.toDp() }
                                            val oldWidth = columnWidths[index] ?: 80.dp
                                            columnWidths[index] = (oldWidth + deltaDp).coerceAtLeast(60.dp)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 2. DATA ROWS (Lazy)
                    itemsIndexed(rows) { rowIndex, rowMap ->
                        val bgColor = if (rowIndex % 2 == 0) BpsSurface else BpsRowAlt

                        Row(
                            modifier = Modifier.background(bgColor),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            headers.forEachIndexed { colIndex, headerKey ->
                                val currentWidth = columnWidths[colIndex] ?: 80.dp
                                val rawValue = rowMap[headerKey]?.toString() ?: "-"
                                val cellValue = rawValue.removeSuffix(".0")

                                TableCell(
                                    text = cellValue,
                                    isHeader = false,
                                    width = currentWidth,
                                    align = if (colIndex == 0) TextAlign.Start else TextAlign.End
                                )

                                // Divider visual antar kolom data
                                if (colIndex < headers.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        VerticalDivider(color = BpsBorderLine, thickness = 1.dp)
                                    }
                                }
                            }
                        }

                        // Garis pemisah antar baris
                        HorizontalDivider(color = BpsBorderLine, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun DraggableDivider(onResize: (Float) -> Unit) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    onResize(dragAmount)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        VerticalDivider(
            modifier = Modifier.height(24.dp),
            color = Color.White.copy(alpha = 0.5f),
            thickness = 2.dp
        )
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
            .padding(horizontal = 8.dp, vertical = 12.dp), // Padding diperbesar agar lebih lega
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
            fontSize = if (isHeader) 11.sp else 12.sp,
            textAlign = align,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun PreviewBetterTable() {
    val dummyData = TableData(
        headers = listOf("Wilayah", "2023", "2024"),
        rows = (1..50).map { // Simulasi 50 data untuk tes scroll
            mapOf(
                "Wilayah" to "Kecamatan $it",
                "2023" to "${1000 + it}",
                "2024" to "${1200 + it}"
            )
        }
    )

    Box(Modifier.padding(16.dp)) {
        TabelDataSection(tableData = dummyData)
    }
}