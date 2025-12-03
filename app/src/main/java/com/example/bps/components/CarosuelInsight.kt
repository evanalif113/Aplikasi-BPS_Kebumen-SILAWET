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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.*
import kotlinx.coroutines.delay

// 1. Data Class untuk SATU variabel (Satu sisi)
data class SingleMetric(
    val iconRes: Int,
    val title: String,
    val value: String,
    val unit: String
)

// 2. Data Class untuk SATU KARTU (Berisi pasangan Kiri & Kanan)
data class InsightPairData(
    val color: Color,
    val left: SingleMetric,
    val right: SingleMetric
)

@Composable
fun CarouselInsight() {
    // 3. Mengelompokkan data per Kartu (Context yang sama)
    val carouselData = listOf(
        // Kartu 1: Kependudukan & Kemiskinan (Sosial)
        InsightPairData(
            color = Blue500,
            left = SingleMetric(R.drawable.ic_lingkungan, "Penduduk", "1.4", "Juta"),
            right = SingleMetric(R.drawable.ic_house_24dp, "Kemiskinan", "16.3", "%")
        ),
        // Kartu 2: Ekonomi (PDRB & Inflasi)
        InsightPairData(
            color = Orange400,
            left = SingleMetric(R.drawable.ic_perangkat_24dp, "Laju PDRB", "5.05", "%"),
            right = SingleMetric(R.drawable.ic_statistik, "Inflasi", "2.61", "%")
        ),
        // Kartu 3: Pembangunan Manusia (IPM & Harapan Hidup)
        InsightPairData(
            color = Green400,
            left = SingleMetric(R.drawable.ic_info_24dp, "IPM", "73.2", "Poin"),
            right = SingleMetric(R.drawable.ic_info_24dp, "UHH", "74.5", "Tahun")
        ),
        // Kartu 4: Ketenagakerjaan (TPT & TPAK)
        InsightPairData(
            color = Red400,
            left = SingleMetric(R.drawable.ic_perangkat_24dp, "TPT", "5.67", "%"),
            right = SingleMetric(R.drawable.ic_perangkat_24dp, "TPAK", "68.2", "%")
        )
    )

    val lazyListState = rememberLazyListState()
    val itemWidth = 300.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2
    val dataSize = carouselData.size

    LaunchedEffect(Unit) {
        val startIndex = Int.MAX_VALUE / 2
        lazyListState.scrollToItem(startIndex - (startIndex % dataSize))

        while (true) {
            delay(6000) // Sedikit lebih lama karena data lebih banyak
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
 * Composable Kartu Utama
 */
@Composable
fun CarouselItem(data: InsightPairData) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(130.dp), // Tinggi disesuaikan agar muat tumpukan vertikal
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = data.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- SISI KIRI ---
            MetricColumn(
                metric = data.left,
                modifier = Modifier.weight(1f)
            )

            // Garis Pemisah Vertikal
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(0.7f) // Garis tidak full sampai atas/bawah
                    .background(White.copy(alpha = 0.4f))
            )

            // --- SISI KANAN ---
            MetricColumn(
                metric = data.right,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Fungsi reusable untuk menampilkan satu kolom variabel (Ikon/Judul/Nilai)
 */
@Composable
fun MetricColumn(metric: SingleMetric, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Baris Atas: Ikon + Judul
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = metric.iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = White.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = metric.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = White.copy(alpha = 0.9f),
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Angka Besar
        Text(
            text = metric.value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = White
        )

        // Satuan Kecil
        Text(
            text = metric.unit,
            style = MaterialTheme.typography.labelSmall,
            color = White.copy(alpha = 0.8f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselItemPairPreview() {
    val dummyPair = InsightPairData(
        color = Blue500,
        left = SingleMetric(R.drawable.ic_lingkungan, "Penduduk", "1.4", "Juta"),
        right = SingleMetric(R.drawable.ic_house_24dp, "Kemiskinan", "16.3", "%")
    )
    Box(modifier = Modifier.padding(16.dp)) {
        CarouselItem(data = dummyPair)
    }
}