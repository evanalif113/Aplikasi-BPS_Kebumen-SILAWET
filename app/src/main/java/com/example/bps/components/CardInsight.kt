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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.R
import com.example.bps.theme.*

/**
 * Composable yang menampilkan daftar kartu secara horizontal (Carousel).
 */
@Composable
fun Carousel() {
    // 2. Menyiapkan daftar data untuk setiap kartu
    val carouselData = listOf(
        Triple(Blue200, R.drawable.ic_grafik_24dp, "Statistik Pertanian"),
        Triple(Green200, R.drawable.ic_perangkat_24dp, "Produk Domestik"),
        Triple(Orange200, R.drawable.ic_server_24dp, "Inflasi Bulanan"),
        Triple(Red200, R.drawable.ic_house_24dp, "Indeks Kemahalan"),
        Triple(Purple200, R.drawable.ic_info_24dp, "Info Lainnya")
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 3. Menampilkan setiap item dari daftar data menggunakan CarouselItem
        items(carouselData.size) { index ->
            val (color, iconRes, title) = carouselData[index]
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
fun CarouselPreview() {
    Carousel()
}

/**
 * Composable untuk satu item kartu di dalam carousel.
 * Komponen ini sekarang dinamis dan bisa digunakan kembali.
 *
 * @param color Warna latar belakang kartu.
 * @param iconRes ID resource drawable untuk ikon.
 * @param title Teks judul untuk kartu.
 */
/**
 * Composable yang menampilkan daftar kartu secara horizontal (Carousel).
 */
@Composable
fun CardInsight() {
    val carouselData = listOf(
        Triple(Blue400, R.drawable.ic_grafik_24dp, "Statistik Pertanian"),
        Triple(Green400, R.drawable.ic_perangkat_24dp, "Produk Domestik"),
        Triple(Orange400, R.drawable.ic_server_24dp, "Inflasi Bulanan"),
        Triple(Red400, R.drawable.ic_house_24dp, "Indeks Kemahalan"),
        Triple(Purple400, R.drawable.ic_info_24dp, "Info Lainnya")
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(carouselData.size) { index ->
            val (color, iconRes, title) = carouselData[index]
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
fun CardInsightPreview() {
    CardInsight()
}

/**
 * Composable untuk satu item kartu di dalam carousel.
 */
@Composable
private fun CarouselItem(
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
                tint = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselItemPreview() {
    CarouselItem(
        color = Blue200,
        iconRes = R.drawable.ic_grafik_24dp,
        title = "Statistik Pertanian"
    )
}