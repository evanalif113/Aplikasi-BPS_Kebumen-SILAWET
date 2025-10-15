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

/**
 * Composable yang menampilkan daftar item secara horizontal (Carousel)
 * dengan animasi auto-scroll yang berhenti di tengah dan berulang (unlimited scroll).
 */
@Composable
fun CarouselInsight() {
    // Data untuk setiap item carousel dengan warna yang lebih kontras
    val carouselData = listOf(
        Triple(Blue400, R.drawable.ic_grafik_24dp, "Statistik Pertanian"),
        Triple(Green400, R.drawable.ic_perangkat_24dp, "Produk Domestik"),
        Triple(Orange400, R.drawable.ic_server_24dp, "Inflasi Bulanan"),
        Triple(Red400, R.drawable.ic_house_24dp, "Indeks Kemahalan"),
        Triple(Purple400, R.drawable.ic_info_24dp, "Info Lainnya")
    )

    val lazyListState = rememberLazyListState()
    val itemWidth = 268.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2
    val dataSize = carouselData.size

    // Efek untuk auto-scroll setiap 5 detik dengan logika unlimited scroll
    LaunchedEffect(Unit) {
        // Mulai dari tengah list "tak terbatas" agar bisa scroll ke kiri dan kanan
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
        // Gunakan Int.MAX_VALUE untuk membuat list seolah-olah tak terbatas
        items(Int.MAX_VALUE) { index ->
            // Gunakan modulo untuk memetakan indeks tak terbatas ke indeks data asli
            val itemIndex = index % dataSize
            val (color, iconRes, title) = carouselData[itemIndex]
            CarouselItem(
                color = color,
                iconRes = iconRes,
                title = title
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
                tint = White // Warna ikon menjadi putih
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = White // Warna teks menjadi putih
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