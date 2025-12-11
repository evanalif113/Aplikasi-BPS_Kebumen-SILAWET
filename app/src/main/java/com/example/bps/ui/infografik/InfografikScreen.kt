package com.example.bps.ui.infografik

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.bps.R
import com.example.bps.ui.general.ContentType
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfografikScreen(
    viewModel: NewsViewModel,
    onNavigateToAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit
) {
    val uiState by viewModel.infografikState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshAllData() },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            when (val state = uiState) {
                is NewsUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is NewsUiState.Error -> {
                    // Tampilkan Error yang jelas
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(painterResource(R.drawable.ic_info_24dp), null, tint = Color.Red)
                            Text("Error: ${state.message}", color = Color.Red)
                            Button(onClick = { viewModel.refreshAllData() }) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }
                is NewsUiState.Success -> {
                    if (state.news.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Belum ada infografis.")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2), // 2 Kolom
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.news) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onNavigateToDetail(item.id, ContentType.INFOGRAFIS)
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column {
                                        // 1. BAGIAN GAMBAR
                                        Box(
                                            modifier = Modifier
                                                .height(200.dp)
                                                .fillMaxWidth()
                                                .background(Color.LightGray) // Warna dasar biar kelihatan kotaknya
                                        ) {
                                            AsyncImage(
                                                model = item.imageUrl,
                                                contentDescription = item.title,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop,
                                                // Tampilkan icon loading saat gambar belum muncul
                                                placeholder = painterResource(R.drawable.ic_placeholder),
                                                // Tampilkan icon error jika gagal load
                                                error = painterResource(R.drawable.ic_placeholder)
                                            )
                                        }

                                        // 2. BAGIAN JUDUL (Penting biar gak kosong melompong)
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                fontSize = 14.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = item.date,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}