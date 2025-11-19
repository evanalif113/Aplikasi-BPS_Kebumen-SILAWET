package com.example.bps.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- IMPORT UNTUK GAMBAR (COIL) ---
import coil.compose.AsyncImage
import coil.request.ImageRequest
// --- IMPORT MODEL DATA (Pastikan path-nya benar) ---
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.data.remote.responses.PublicationItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabbedContentSection(
    modifier: Modifier = Modifier,
    // Tab 1: Publikasi (Buku)
    publicationList: List<PublicationItem> = emptyList(),
    // Tab 2: Berita Resmi Statistik (Pakai NewsItem)
    brsList: List<NewsItem> = emptyList(),
    // Tab 3: Infografis (Pakai NewsItem)
    infografikList: List<NewsItem> = emptyList()
) {
    // Nama Tab
    val tabs = listOf("Publikasi", "BRS", "Infografis")

    // State untuk Pager (Geser Halaman)
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {

        // --- HEADER: JUDUL & TAB ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Rilis Data Terbaru",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Tab Navigation
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {}, // Hilangkan garis bawah panjang default
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
                        onClick = {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- ISI KONTEN (YANGb BISA DIGESER) ---
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> PublicationList(publicationList) // Tab 1: Tampilan Buku
                1 -> NewsList(brsList, isInfographic = false) // Tab 2: Tampilan Berita
                2 -> NewsList(infografikList, isInfographic = true) // Tab 3: Tampilan Infografis
            }
        }
    }
}

// --- SUB-COMPONENT 1: LIST BUKU (PUBLIKASI) ---
@Composable
fun PublicationList(items: List<PublicationItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Jika data kosong, bisa tampilkan placeholder atau kosong saja
        if (items.isEmpty()) {
            // Opsional: Tampilkan dummy saat loading/kosong
            items(3) { DummyPublicationCard() }
        } else {
            items(items) { item ->
                PublicationCard(item)
            }
        }
    }
}

// --- SUB-COMPONENT 2: LIST BERITA/INFOGRAFIS ---
@Composable
fun NewsList(items: List<NewsItem>, isInfographic: Boolean) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (items.isEmpty()) {
            items(3) { DummyMiniNewsCard(isInfographic) }
        } else {
            items(items) { item ->
                MiniNewsCard(item, isInfographic)
            }
        }
    }
}

// --- UI KARTU: PUBLIKASI (MODEL BUKU BERDIRI) ---
@Composable
fun PublicationCard(item: PublicationItem) {
    Column(modifier = Modifier.width(110.dp)) {
        // Gambar Cover (Vertikal)
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.coverUrl) // Mengambil URL Cover
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Judul
        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )

        // Tanggal (Menggunakan helper dari PublicationItem.kt)
        Text(
            text = item.getSimpleDate(),
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

// --- UI KARTU: BERITA & INFOGRAFIS (MODEL LANDSCAPE) ---
@Composable
fun MiniNewsCard(item: NewsItem, isInfographic: Boolean) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            // Gambar Thumbnail (Menggunakan helper dari NewsItem.kt)
            Box(modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .background(Color.LightGray) // Background sementara loading
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.getDisplayImage()) // Helper otomatis pilih gambar
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Teks Info
            Column(modifier = Modifier.padding(10.dp)) {
                // Label Kategori
                Text(
                    text = if (isInfographic) "Infografis" else "Berita Resmi",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                // Judul
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                // Tanggal (Menggunakan helper)
                Text(
                    text = item.getSimpleDate(),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// --- DUMMY UNTUK PREVIEW (Opsional) ---
@Composable
fun DummyPublicationCard() {
    // Placeholder UI jika data kosong
    Column(modifier = Modifier.width(110.dp)) {
        Box(modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(Color.Gray, RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.height(12.dp).fillMaxWidth().background(Color.LightGray))
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.height(10.dp).width(50.dp).background(Color.LightGray))
    }
}

@Composable
fun DummyMiniNewsCard(isInfographic: Boolean) {
    // Placeholder UI jika data kosong
    Box(modifier = Modifier
        .width(220.dp)
        .height(150.dp)
        .background(Color.LightGray, RoundedCornerShape(12.dp)))
}

@Preview(showBackground = true)
@Composable
fun TabbedSectionPreview() {
    TabbedContentSection()
}