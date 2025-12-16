package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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

@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
    val rows = tableData.rows
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    // State Lebar Kolom
    val columnWidths = remember { mutableStateMapOf<Int, Dp>() }

    // Inisialisasi Lebar Awal (Update jika headers berubah)
    LaunchedEffect(headers) {
        headers.forEachIndexed { index, _ ->
            if (!columnWidths.containsKey(index)) {
                // PERBAIKAN: Kolom pertama dikasih napas (140dp), jangan 70dp nanti bengek.
                columnWidths[index] = if (index == 0) 140.dp else 70.dp
            }
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
        // --- PERBAIKAN LOGIKA SCROLL ---
        // Scroll ditaruh di Parent Column, bukan di tiap baris.
        // Jadi tabelnya geser barengan sebadan-badan.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState) // <--- Scroll Handler Pindah Sini
        ) {

            // 1. HEADER ROW
            Row(
                modifier = Modifier.background(BpsHeaderBg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                headers.forEachIndexed { index, header ->
                    val currentWidth = columnWidths[index] ?: 70.dp
                    val cleanHeader = header.removeSuffix(".0")

                    TableCell(
                        text = cleanHeader.uppercase(),
                        isHeader = true,
                        width = currentWidth,
                        align = if (index == 0) TextAlign.Start else TextAlign.Center
                    )

                    // Resizer (Pembatas Drag)
                    if (index < headers.size - 1) {
                        DraggableDivider(
                            onResize = { dragAmountPx ->
                                val deltaDp = with(density) { dragAmountPx.toDp() }
                                val oldWidth = columnWidths[index] ?: 70.dp
                                columnWidths[index] = (oldWidth + deltaDp).coerceAtLeast(50.dp)
                            }
                        )
                    }
                }
            }

            // 2. DATA BODY ROWS
            Column {
                rows.forEachIndexed { rowIndex, rowMap ->
                    val bgColor = if (rowIndex % 2 == 0) BpsSurface else BpsRowAlt

                    Row(
                        modifier = Modifier.background(bgColor),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        headers.forEachIndexed { colIndex, headerKey ->
                            val currentWidth = columnWidths[colIndex] ?: 70.dp
                            val rawValue = rowMap[headerKey]?.toString() ?: "-"
                            val cellValue = rawValue.removeSuffix(".0")

                            TableCell(
                                text = cellValue,
                                isHeader = false,
                                width = currentWidth,
                                align = if (colIndex == 0) TextAlign.Start else TextAlign.End
                            )

                            // Pembatas Visual (Agar lurus sama header)
                            if (colIndex < headers.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(10.dp) // Lebar dummy harus sama dengan DraggableDivider
                                        .height(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    VerticalDivider(color = BpsBorderLine, thickness = 1.dp)
                                }
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
fun DraggableDivider(onResize: (Float) -> Unit) {
    Box(
        modifier = Modifier
            .width(10.dp) // Area sentuh jari
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
            .padding(horizontal = 4.dp, vertical = 8.dp),
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
            fontSize = if (isHeader) 10.sp else 12.sp,
            textAlign = align,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun PreviewResizableTable() {
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