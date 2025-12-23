package com.example.bps.components

import androidx.compose.foundation.Image // Import Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.data.remote.responses.IndicatorItem
import com.example.bps.theme.*
import kotlinx.coroutines.delay

@Composable
fun CarouselInsight(
    indicators: List<IndicatorItem>,
    onItemClick: (IndicatorItem) -> Unit = {}
) {
    if (indicators.isEmpty()) return

    val lazyListState = rememberLazyListState()
    val itemWidth = 280.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val contentPadding = (screenWidth - itemWidth) / 2

    LaunchedEffect(indicators) {
        if (indicators.isNotEmpty()) {
            val startIndex = Int.MAX_VALUE / 2
            val startOffset = startIndex - (startIndex % indicators.size)
            lazyListState.scrollToItem(startOffset)

            while (true) {
                delay(4000)
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
        items(
            count = Int.MAX_VALUE,
            key = { index -> index }
        ) { index ->
            if (indicators.isNotEmpty()) {
                val itemIndex = index % indicators.size
                val item = indicators[itemIndex]
                CarouselItem(item = item, onClick = { onItemClick(item) })
            }
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
                            // Hapus 'tint = Color.White'
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = item.getLabel().uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.9f),
                        letterSpacing = 1.sp,
                        maxLines = 1
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
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Tahun ${item.year}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White
                    )
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

        else -> Pair(Gray500, R.drawable.ic_info_24dp)
    }
}