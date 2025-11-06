package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bps.data.remote.responses.TableData

private val CellWidth = 120.dp

/**
 * Composable "bodoh" yang hanya bertugas menampilkan data tabel. Dibuat agar bisa di-scroll
 * horizontal jika kolomnya banyak.
 *
 * @param tableData Objek TableData yang berisi headers dan rows.
 */
@Composable
fun TabelDataSection(tableData: TableData, modifier: Modifier = Modifier) {
    val hScroll = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        // Satu-satunya horizontalScroll: membungkus header + baris
        Box(modifier = Modifier.fillMaxWidth().horizontalScroll(hScroll)) {
            Column {
                // 1) Header
                Row(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(vertical = 4.dp)
                ) {
                    tableData.headers.forEach { header ->
                        Text(
                                text = header,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier =
                                        Modifier.width(CellWidth)
                                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                // 2) Baris data (scroll vertikal saja)
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(tableData.rows) { rowMap ->
                        Row(modifier = Modifier.fillMaxWidth().border(0.5.dp, Color.LightGray)) {
                            tableData.headers.forEach { headerKey ->
                                val cellValue = rowMap[headerKey]
                                Text(
                                        text = cellValue?.toString() ?: "-",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier =
                                                Modifier.width(CellWidth)
                                                        .padding(
                                                                horizontal = 8.dp,
                                                                vertical = 10.dp
                                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
