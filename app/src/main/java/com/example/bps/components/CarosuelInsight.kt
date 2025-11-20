package com.example.bps.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import com.example.bps.R
import com.example.bps.theme.*
import kotlinx.coroutines.delay

// 1. Membuat Data Class untuk menggantikan Triple
data class InsightData(
    val color: Color,
    val iconRes: Int,
    val title: String
)

/**
 * Composable yang menampilkan daftar item secara horizontal (Carousel)
 * dengan animasi auto-scroll yang berhenti di tengah dan berulang (unlimited scroll).
 */
@Composable
fun CarouselInsight() {
    // 2. Menggunakan InsightData untuk daftar item
    val carouselData = listOf(
        InsightData(Blue500, R.drawable.ic_lingkungan, "Statistik Pertanian"),
        InsightData(Green400, R.drawable.ic_perangkat_24dp, "Produk Domestik"),
        InsightData(Orange400, R.drawable.ic_statistik, "Inflasi Bulanan"),
        InsightData(Red400, R.drawable.ic_house_24dp, "Indeks Kemiskinan"),
        InsightData(Purple400, R.drawable.ic_info_24dp, "Info Lainnya")
    )

    val lazyListState = rememberLazyListState()
    val itemWidth = 268.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2
    val dataSize = carouselData.size

    // Efek untuk auto-scroll setiap 5 detik dengan logika unlimited scroll
    LaunchedEffect(Unit) {
        val startIndex = Int.MAX_VALUE / 2
        lazyListState.scrollToItem(startIndex - (startIndex % dataSize))

        while (true) {
            delay(5000) // Jeda 5 detik
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
            // 3. Mengambil data object dan mengakses propertinya
            val item = carouselData[itemIndex]

            CarouselItem(
                color = item.color,
                iconRes = item.iconRes,
                title = item.title
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselInsightPreview() {
    CarouselInsight()
}

/**
 * Composable untuk satu item kartu di dalam carousel dengan tampilan yang diperbaiki.
 */
@Composable
fun CarouselItem(
    color: Color,
    iconRes: Int,
    title: String
) {
    Card(
        modifier = Modifier
            .width(268.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselItemPreview() {
    CarouselItem(
        color = Blue400,
        iconRes = R.drawable.ic_grafik_24dp,
        title = "Statistik Pertanian"
    )
}