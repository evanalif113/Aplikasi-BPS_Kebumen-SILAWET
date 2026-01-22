package com.example.bps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import com.example.bps.R
import com.example.bps.data.remote.responses.IndicatorItem
import com.example.bps.theme.*
import kotlinx.coroutines.delay

@Composable
fun CarouselInsight(
    indicators: List<IndicatorItem>,
    isLoading: Boolean, // <--- Tambahkan parameter ini
    onItemClick: (IndicatorItem) -> Unit = {}
) {
    val lazyListState = rememberLazyListState()
    val itemWidth = 280.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2

    // Auto scroll hanya jalan jika TIDAK loading dan data TIDAK kosong
    LaunchedEffect(indicators, isLoading) {
        if (!isLoading && indicators.isNotEmpty()) {
            val startIndex = Int.MAX_VALUE / 2
            val startOffset = startIndex - (startIndex % indicators.size)
            lazyListState.scrollToItem(startOffset)

            while (true) {
                delay(4000)
                // Cek ulang untuk menghindari crash saat recomposition
                if (indicators.isNotEmpty()) {
                    val nextIndex = lazyListState.firstVisibleItemIndex + 1
                    lazyListState.animateScrollToItem(index = nextIndex)
                }
            }
        }
    }

    LazyRow(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // --- LOGIKA UTAMA DISINI ---
        if (isLoading) {
            // Tampilkan 3 Skeleton Dummy jika sedang loading
            items(3) {
                SkeletonCarouselItem()
            }
        } else if (indicators.isNotEmpty()) {
            // Tampilkan Data Asli (Looping Infinite)
            items(
                count = Int.MAX_VALUE,
                key = { index -> index }
            ) { index ->
                val itemIndex = index % indicators.size
                val item = indicators[itemIndex]
                CarouselItem(item = item, onClick = { onItemClick(item) })
            }
        }
    }
}

@Composable
fun SkeletonCarouselItem() {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), // Warna dasar putih
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Header Skeleton (Icon + Title)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Kotak Icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(10.dp))
                // Garis Judul
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }

            // 2. Value Skeleton (Angka Besar)
            Row(verticalAlignment = Alignment.Bottom) {
                // Angka Besar
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Satuan Kecil
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }

            // 3. Footer Skeleton (Tahun)
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun CarouselItem(
    item: IndicatorItem,
    onClick: () -> Unit
) {
    val (bgColor, iconRes) = getStyleBySlug(item.slug)

    Card(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- PERBAIKAN 1: Hiasan Latar Belakang ---
            // Ganti Icon -> Image agar warna asli terlihat.
            // Gunakan alpha untuk transparansi.
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                alpha = 0.75f, // Transparansi 15%
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Header: Icon Kecil + Judul
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // --- PERBAIKAN 2: Icon Header ---
                        // Ganti Icon -> Image agar warna asli (full color)
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = item.getLabel().uppercase(),
                        style = MaterialTheme.typography.labelMedium, // Ganti ke labelMedium (lebih kecil dari labelLarge)
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 0.5.sp, // Kurangi letter spacing agar muat lebih banyak
                        maxLines = 2, // Tetap 1 baris agar layout bawah tidak bergeser
                        overflow = TextOverflow.Ellipsis // Potong cantik
                    )
                }

                // 2. Nilai Utama
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = item.getFormattedValue(),
                        style = MaterialTheme.typography.displaySmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.getFormattedUnit(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // 3. Footer: Tahun
                Box(
                    modifier =
                            Modifier.background(Color.Black.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    // Logic: Jika year null, tampilkan strip "-", jika ada tampilkan angkanya
                    val yearText = item.year?.toString() ?: "-"

                    Text(text = "Tahun $yearText", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }

            }
        }
    }
}

@Composable
fun getStyleBySlug(slug: String): Pair<Color, Int> {
    return when (slug) {
        "kependudukan" -> Pair(Blue500, R.drawable.penduduk)
        "tenaga-kerja" -> Pair(Red400, R.drawable.tenaga_kerja)
        "pengangguran" -> Pair(Red400, R.drawable.pengangguran)
        "kemiskinan" -> Pair(Orange400, R.drawable.kemiskinan)
        "rasio-gini" -> Pair(Color(0xFF9C27B0), R.drawable.gini_rasio_dan_ketimpangan)
        "ipm" -> Pair(Green400, R.drawable.ipm_ipg_idg)
        "ekonomi" -> Pair(Color(0xFFFFC107), R.drawable.pertumbuhan_ekonomi)
        "pdrb" -> Pair(Color(0xFFFFC107), R.drawable.pdrb)
        "pendidikan" -> Pair(Color(0xFF009688), R.drawable.pendidikan)
        "perumahan" -> Pair(Color(0xFF009688), R.drawable.perumahan)
        "inflasi" -> Pair(Color(0xFF009688), R.drawable.inflasi)

        else -> Pair(Gray500, R.drawable.ic_info_24dp)
    }
}