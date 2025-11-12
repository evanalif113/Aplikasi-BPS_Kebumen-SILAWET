package com.example.bps.ui.infografik

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.InfografikSection
import com.example.bps.components.SearchBar
import com.example.bps.components.NewsSection

// --- PERBAIKAN IMPORT ---
// Lokasi yang benar, sesuai dengan MainActivity.kt Anda
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.NewsViewModel
// -------------------------

@Composable
fun InfografikScreen(
    // --- PERBAIKAN PARAMETER ---
    // 1. Ganti nama 'newsViewModel' menjadi 'viewModel' agar cocok dengan panggilan di MainActivity
    // 2. Hapus `= viewModel()` agar tidak membuat instance baru
    viewModel: NewsViewModel,
    onNavigateToAllNews: () -> Unit
    // ---------------------------
) {
    // Ambil state dari ViewModel yang DIOPERKAN
    val newsUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))

        // Ini mungkin section statis (tanpa parameter), jadi ini OK
        InfografikSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Panggil NewsSection dan masukkan kedua parameter
        NewsSection(
            uiState = newsUiState,
            onSeeAllClicked = onNavigateToAllNews
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun InfografikScreenPreview() {

    InfografikScreen(
        viewModel = viewModel(), // <-- Tambahkan ini
        onNavigateToAllNews = {}
    )
}