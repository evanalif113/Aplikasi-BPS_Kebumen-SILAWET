package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.*
import kotlinx.coroutines.delay

// 1. Update Data Class: Tambahkan field 'value' (angka) dan 'unit' (satuan)
data class InsightData(
    val color: Color,
    val iconRes: Int,
    val title: String,
    val value: String, // Contoh: "1.4 Juta"
    val unit: String   // Contoh: "Jiwa"
)

@Composable
fun CarouselInsight() {
    // 2. Isi data dengan angka dummy statistik
    val carouselData = listOf(
        InsightData(Blue500, R.drawable.ic_lingkungan, "Penduduk", "1.4", "Juta Jiwa"),
        InsightData(Green400, R.drawable.ic_perangkat_24dp, "PDRB", "5.05", "% (Laju)"),
        InsightData(Orange400, R.drawable.ic_statistik, "Inflasi", "2.61", "% (y-on-y)"),
        InsightData(Red400, R.drawable.ic_house_24dp, "Kemiskinan", "16.3", "%"),
        InsightData(Purple400, R.drawable.ic_info_24dp, "IPM", "73.2", "Poin")
    )

    val lazyListState = rememberLazyListState()
    val itemWidth = 280.dp // Sedikit diperlebar agar muat 2 kolom
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2
    val dataSize = carouselData.size

    LaunchedEffect(Unit) {
        val startIndex = Int.MAX_VALUE / 2
        lazyListState.scrollToItem(startIndex - (startIndex % dataSize))

        while (true) {
            delay(5000)
            val nextIndex = lazyListState.firstVisibleItemIndex + 1
            lazyListState.animateScrollToItem(index = nextIndex)
        }
    }

    LazyRow(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = contentPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(Int.MAX_VALUE) { index ->
            val itemIndex = index % dataSize
            val item = carouselData[itemIndex]

            CarouselItem(data = item)
        }
    }
}

/**
 * Composable Kartu yang dibagi menjadi 2 bagian (Kiri: Info, Kanan: Data)
 */
@Composable
fun CarouselItem(data: InsightData) {
    Card(
        modifier = Modifier
            .width(280.dp) // Lebar kartu
            .height(110.dp), // Tinggi kartu
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = data.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Row utama untuk membagi kartu menjadi Kiri dan Kanan
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // Padding dalam kartu
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- BAGIAN KIRI (Ikon & Judul) ---
            Column(
                modifier = Modifier
                    .weight(1f) // Mengambil 50% lebar
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Box putih transparan di belakang ikon agar ikon lebih menonjol
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = data.iconRes),
                        contentDescription = data.title,
                        modifier = Modifier.size(24.dp),
                        tint = White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = White.copy(alpha = 0.9f)
                )
            }

            // Garis Pemisah Vertikal Tipis (Opsional)
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(0.8f)
                    .background(White.copy(alpha = 0.3f))
            )

            // --- BAGIAN KANAN (Angka Data) ---
            Column(
                modifier = Modifier
                    .weight(1f) // Mengambil 50% lebar sisa
                    .padding(start = 12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End // Rata kanan agar rapi
            ) {
                Text(
                    text = data.value,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp), // Angka Besar
                    fontWeight = FontWeight.Bold,
                    color = White
                )
                Text(
                    text = data.unit,
                    style = MaterialTheme.typography.labelSmall,
                    color = White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselItemPreview() {
    val dummyData = InsightData(
        color = Blue500,
        iconRes = R.drawable.ic_grafik_24dp,
        title = "Pertumbuhan",
        value = "5.05",
        unit = "% (y-on-y)"
    )
    Box(modifier = Modifier.padding(16.dp)) {
        CarouselItem(data = dummyData)
    }
}