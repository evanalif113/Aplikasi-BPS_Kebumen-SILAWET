package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Import clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.bps.data.remote.responses.NewsItemResponse
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.theme.Gray200

@Composable
fun NewsCard(
    newsItem: NewsItemResponse,
    onClick: () -> Unit // 1. Tambahkan parameter onClick
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick), // 2. Pasang aksi klik di Card
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Gambar
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Gray200)
            ) {
                AsyncImage(
                    model = newsItem.thumbnailUrl,
                    contentDescription = newsItem.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_placeholder),
                    error = painterResource(id = R.drawable.ic_placeholder)
                )
            }

            // Konten Teks
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = newsItem.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = newsItem.title,
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

@Composable
fun NewsSection(
    uiState: NewsUiState,
    onSeeAllClicked: () -> Unit,
    onItemClicked: (Int) -> Unit, // 3. Tambahkan parameter callback dengan ID
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
                text = "Berita dan Siaran Pers",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lihat Semua",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onSeeAllClicked)
            )
        }

        when (uiState) {
            is NewsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NewsUiState.Success -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.news.take(5)) { newsItem ->
                        NewsCard(
                            newsItem = newsItem,
                            // 4. Panggil callback saat kartu diklik
                            onClick = { onItemClicked(newsItem.id) }
                        )
                    }
                }
            }
            is NewsUiState.Error -> {
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
    val dummyNewsList = listOf(
        NewsItemResponse(
            id = 1,
            date = "9 Oktober 2025",
            category = "Kegiatan",
            title = "Selamat Hari Pos Sedunia!",
            abstract = "...",
            thumbnailUrl = "...",
            link = ""
        )
    )

    NewsSection(
        uiState = NewsUiState.Success(dummyNewsList),
        onSeeAllClicked = {},
        onItemClicked = {} // 5. Tambahkan lambda kosong untuk preview
    )
}