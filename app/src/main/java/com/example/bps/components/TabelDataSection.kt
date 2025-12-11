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
val BpsDividerHover = Color(0xFFFF9800) // Warna indikator saat digeser (Opsional)

@Composable
fun TabelDataSection(
    tableData: TableData,
    modifier: Modifier = Modifier
) {
    val headers = tableData.headers
    val rows = tableData.rows
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    // --- STATE LEBAR KOLOM ---
    // Kita simpan lebar setiap kolom di sini.
    // Key: Index Kolom, Value: Lebar dalam Dp
    val columnWidths = remember { mutableStateMapOf<Int, Dp>() }

    // Inisialisasi Lebar Awal (Hanya sekali saat pertama render)
    LaunchedEffect(Unit) {
        headers.forEachIndexed { index, _ ->
            if (!columnWidths.containsKey(index)) {
                // Default: Kolom pertama 140dp, sisanya 65dp
                columnWidths[index] = if (index == 0) 140.dp else 65.dp
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
        Column(modifier = Modifier.fillMaxWidth()) {

            // 1. HEADER ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BpsHeaderBg)
                    .horizontalScroll(scrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                headers.forEachIndexed { index, header ->
                    val cleanHeader = header.substringBefore(".")
                    val currentWidth = columnWidths[index] ?: 65.dp

                    // Sel Header
                    TableCell(
                        text = cleanHeader.uppercase(),
                        isHeader = true,
                        width = currentWidth,
                        align = if (index == 0) TextAlign.Start else TextAlign.Center
                    )

                    // Pembatas yang bisa digeser (Resizer)
                    if (index < headers.size - 1) {
                        DraggableDivider(
                            onResize = { dragAmountPx ->
                                // Konversi pixel geseran ke Dp
                                val deltaDp = with(density) { dragAmountPx.toDp() }
                                val oldWidth = columnWidths[index] ?: 65.dp
                                // Update lebar kolom, batasi minimal 40.dp agar tidak hilang
                                columnWidths[index] = (oldWidth + deltaDp).coerceAtLeast(40.dp)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor)
                            .horizontalScroll(scrollState), // Sinkron scroll dengan header
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        headers.forEachIndexed { colIndex, headerKey ->
                            val cellValue = rowMap[headerKey]?.toString() ?: "-"
                            val currentWidth = columnWidths[colIndex] ?: 65.dp

                            // Sel Data
                            TableCell(
                                text = cellValue,
                                isHeader = false,
                                width = currentWidth,
                                align = if (colIndex == 0) TextAlign.Start else TextAlign.End
                            )

                            // Pembatas (Hanya visual di baris data, agar lurus dengan header)
                            if (colIndex < headers.size - 1) {
                                // Kita gunakan Box transparan selebar area drag header (10.dp)
                                // agar garisnya lurus vertikal
                                Box(
                                    modifier = Modifier
                                        .width(10.dp) // Samakan dengan lebar area sentuh DraggableDivider
                                        .height(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    VerticalDivider(
                                        color = BpsBorderLine,
                                        thickness = 1.dp
                                    )
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

/**
 * Komponen Pembatas yang Bisa Digeser
 */
@Composable
fun DraggableDivider(
    onResize: (Float) -> Unit
) {
    // Box pembungkus untuk area sentuh yang lebih luas (Hitbox)
    Box(
        modifier = Modifier
            .width(10.dp) // Lebar area sentuh jari (bukan lebar garis visual)
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume() // Konsumsi event agar tidak dianggap scroll
                    onResize(dragAmount)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Garis Visual (Tipis)
        VerticalDivider(
            modifier = Modifier.height(24.dp),
            color = Color.White.copy(alpha = 0.5f), // Warna garis header
            thickness = 2.dp // Sedikit dipertebal agar terlihat bisa digeser
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