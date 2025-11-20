package com.example.bps.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController // Pastikan import ini ada
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.PublicationUiState
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.data.remote.responses.PublicationItem

// Enum untuk menentukan Tipe Konten
enum class ContentType {
    NEWS, BRS, INFOGRAFIS, PUBLIKASI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralListScreen(
    navController: NavController,
    viewModel: NewsViewModel,
    contentType: ContentType,
    title: String
) {
    val newsState by viewModel.newsState.collectAsState()
    val brsState by viewModel.brsState.collectAsState()
    val infografikState by viewModel.infografikState.collectAsState()
    val publicationState by viewModel.publicationState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF5F5F5))) {

            // LOGIKA PEMILIHAN DATA
            when (contentType) {
                ContentType.PUBLIKASI -> {
                    // Kirim navController ke Helper
                    HandlePublicationState(publicationState, navController)
                }
                ContentType.NEWS -> {
                    // Kirim navController dan Tipe ke Helper
                    HandleNewsState(newsState, navController, ContentType.NEWS)
                }
                ContentType.BRS -> {
                    HandleNewsState(brsState, navController, ContentType.BRS)
                }
                ContentType.INFOGRAFIS -> {
                    HandleNewsState(infografikState, navController, ContentType.INFOGRAFIS)
                }
            }
        }
    }
}

// --- HELPER: MENANGANI STATE PUBLIKASI ---
@Composable
fun HandlePublicationState(state: PublicationUiState, navController: NavController) {
    when (state) {
        is PublicationUiState.Loading -> LoadingView()
        is PublicationUiState.Error -> ErrorView(state.message)
        is PublicationUiState.Success -> {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.data) { item ->
                    // Kirim navController ke Item
                    PublicationListItem(item, navController)
                }
            }
        }
    }
}

// --- HELPER: MENANGANI STATE NEWS (Berita, BRS, Infografis) ---
@Composable
fun HandleNewsState(state: NewsUiState, navController: NavController, type: ContentType) {
    when (state) {
        is NewsUiState.Loading -> LoadingView()
        is NewsUiState.Error -> ErrorView(state.message)
        is NewsUiState.Success -> {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.news) { item ->
                    // Kirim navController & Tipe ke Item
                    NewsListItem(item, navController, type)
                }
            }
        }
    }
}

// --- UI ITEM: BUKU (Row) ---
@Composable
fun PublicationListItem(item: PublicationItem, navController: NavController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                navController.navigate("detail_content/${item.id}/${ContentType.PUBLIKASI}")
            }
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            // Gambar Cover Buku
            Card(shape = RoundedCornerShape(4.dp), modifier = Modifier.width(70.dp).fillMaxHeight()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(item.coverUrl).crossfade(true).build(),
                    contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Teks
            Column(verticalArrangement = Arrangement.Center) {
                Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.getSimpleDate(), fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

// --- UI ITEM: BERITA/INFOGRAFIS (Row) ---
@Composable
fun NewsListItem(item: NewsItem, navController: NavController, type: ContentType) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable {
                // --- PERBAIKAN: NAVIGASI KE DETAIL ---
                navController.navigate("detail_content/${item.id}/$type")
            }
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            // Gambar Thumbnail
            Card(shape = RoundedCornerShape(8.dp), modifier = Modifier.width(100.dp).fillMaxHeight()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(item.getDisplayImage()).crossfade(true).build(),
                    contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Teks
            Column {
                Text(item.getSimpleDate(), fontSize = 11.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// --- LOADING & ERROR VIEWS ---
@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(msg: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Error: $msg", color = Color.Red)
    }
}