package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.bps.R
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.theme.Gray200
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.clickable


@Composable
fun NewsCard(newsItem: NewsItem) { // <-- Diperbaiki: Menerima NewsItem dari API
    Card(
        modifier = Modifier.width(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Gambar dari URL menggunakan Coil AsyncImage
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Gray200)
            ) {
                AsyncImage(
                    model = newsItem.thumbnailUrl, // <-- Diperbaiki: Data dari NewsItem
                    contentDescription = newsItem.title, // <-- Diperbaiki: Data dari NewsItem
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    // Tambahkan placeholder jika gambar gagal dimuat
                    placeholder = painterResource(id = R.drawable.ic_placeholder), // <-- GANTI dengan drawable Anda
                    error = painterResource(id = R.drawable.ic_placeholder) // <-- GANTI dengan drawable Anda
                )
            }

            // Konten teks di bawah gambar
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = newsItem.date, // <-- Diperbaiki: Data dari NewsItem
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = newsItem.title, // <-- Diperbaiki: Data dari NewsItem
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// Composable untuk seluruh bagian "Berita dan Pers"
@Composable
fun NewsSection(
    uiState: NewsUiState,
    onSeeAllClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Berita & Rilis Pers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lihat Semua",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary, // Warna tema
                modifier = Modifier.clickable(onClick = onSeeAllClicked) // Aksi klik
            )
        }

        // Tampilkan UI berdasarkan state
        when (uiState) {
            is NewsUiState.Loading -> {
                // Tampilkan loading di tengah
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp), // Samakan tinggi dengan kartu
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NewsUiState.Success -> {
                // Tampilkan daftar berita jika sukses
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.news.take(5)) { newsItem ->
                        NewsCard(newsItem = newsItem)
                    }
                }
            }
            is NewsUiState.Error -> {
                // Tampilkan pesan error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = Color.Red,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsSectionPreview() {
    // Buat data dummy NewsItem untuk preview
    val dummyNewsList = listOf(
        NewsItem(
            id = 1,
            date = "9 Oktober 2025",
            category = "Kegiatan",
            title = "Selamat Hari Pos Sedunia!",
            abstract = "...",
            thumbnailUrl = "...", // Isi dengan URL gambar dummy jika ada
            link = "",
            createdAt = "",
            updatedAt = ""
        ),
        NewsItem(
            id = 2,
            date = "29 Oktober 2025",
            category = "Kegiatan",
            title = "Forum Satu Data Kabupaten Kebumen",
            abstract = "...",
            thumbnailUrl = "...", // Isi dengan URL gambar dummy jika ada
            link = "",
            createdAt = "",
            updatedAt = ""
        )
    )

    // Tampilkan NewsSection dengan state Success
    NewsSection(
        uiState = NewsUiState.Success(dummyNewsList),
        onSeeAllClicked = {} // <-- TAMBAHKAN INI
    )
}

@Preview(showBackground = true)
@Composable
fun NewsSectionLoadingPreview() {
    // Tampilkan NewsSection dengan state Loading
    NewsSection(
        uiState = NewsUiState.Loading,
        onSeeAllClicked = {} // <-- TAMBAHKAN INI
    )
}

@Preview(showBackground = true)
@Composable
fun NewsSectionErrorPreview() {
    // Tampilkan NewsSection dengan state Error
    NewsSection(
        uiState = NewsUiState.Error("Gagal memuat berita."),
        onSeeAllClicked = {} // <-- TAMBAHKAN INI
    )
}