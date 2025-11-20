package com.example.bps.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.data.remote.responses.PublicationItem
import com.example.bps.ui.common.ContentType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabbedContentSection(
    modifier: Modifier = Modifier,
    publicationList: List<PublicationItem> = emptyList(),
    brsList: List<NewsItem> = emptyList(),
    infografikList: List<NewsItem> = emptyList(),
    onItemClick: (Int, ContentType) -> Unit
) {
    val tabs = listOf("Publikasi", "BRS", "Infografis")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {

        // --- HEADER & TAB ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rilis Data Terbaru",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                // Tombol Lihat Semua (Opsional)
                Text(
                    text = "Lihat Semua",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* Aksi navigasi */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Navigation
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            height = 3.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ISI KONTEN (Pager) ---
        // Kita atur tinggi fix atau biarkan wrap content
        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { page ->
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (page) {
                    0 -> VerticalPublicationList(publicationList, onItemClick) // Kirim callback
                    1 -> VerticalNewsList(brsList, ContentType.BRS, onItemClick) // Kirim callback + Tipe
                    2 -> VerticalNewsList(infografikList, ContentType.INFOGRAFIS, onItemClick) // Kirim callback + Tipe
                }
            }
        }
    }
}

// --- LIST VERTIKAL (PUBLIKASI) ---
@Composable
fun VerticalPublicationList(items: List<PublicationItem>, onClick: (Int, ContentType) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items.take(3).forEach { item ->
            // Panggil onClick dengan Tipe PUBLIKASI
            PublicationRowItem(item, onClick = { onClick(item.id, ContentType.PUBLIKASI) })
        }
    }
}

@Composable
fun VerticalNewsList(items: List<NewsItem>, type: ContentType, onClick: (Int, ContentType) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items.take(3).forEach { item ->
            // Panggil onClick dengan Tipe yang sesuai (BRS/INFOGRAFIS)
            NewsRowItem(item, onClick = { onClick(item.id, type) })
        }
    }
}

// --- DESAIN BARU: LIST ITEM (GAMBAR KIRI, TEKS KANAN) ---

@Composable
fun PublicationRowItem(item: PublicationItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable { onClick() }, // <--- Pasang Klik di sini
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ... (Isi UI Row sama seperti sebelumnya) ...
        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.width(70.dp).fillMaxHeight()) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(item.coverUrl).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Text(item.getSimpleDate(), fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 3, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)
        }
    }
}

@Composable
fun NewsRowItem(item: NewsItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable { onClick() }, // <--- Pasang Klik di sini
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ... (Isi UI Row sama seperti sebelumnya) ...
        Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.width(100.dp).fillMaxHeight()) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(item.getDisplayImage()).crossfade(true).build(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Top) {
            Text(item.getSimpleDate(), fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)
        }
    }
}

@Composable
fun DummyRowItem() {
    Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        Box(modifier = Modifier.width(80.dp).fillMaxHeight().background(Color.LightGray, RoundedCornerShape(8.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.height(12.dp).width(60.dp).background(Color.LightGray))
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(16.dp).fillMaxWidth().background(Color.LightGray))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabbedSectionPreview() {
    TabbedContentSection(
        onItemClick = { id, type ->
        }
    )
}