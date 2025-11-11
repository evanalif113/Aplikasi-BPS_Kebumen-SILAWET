package com.example.bps.ui.infografik

// Untuk fungsi viewModel()
import androidx.lifecycle.viewmodel.compose.viewModel

// Untuk fungsi collectAsState()
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // Biasanya diperlukan saat menggunakan 'by'
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
import com.example.bps.ui.news.NewsUiState
import com.example.bps.ui.news.NewsViewModel

@Composable
fun InfografikScreen(
    // 1. Inisialisasi ViewModel di level Screen
    newsViewModel: NewsViewModel = viewModel()
) {
    // 2. Ambil (collect) state dari ViewModel
    val newsUiState by newsViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))

        InfografikSection()

        Spacer(modifier = Modifier.height(24.dp)) // Beri jarak antar section

        // 3. Panggil NewsSection dan MASUKKAN uiState
        NewsSection(uiState = newsUiState) // <-- INI PERBAIKANNYA

        Spacer(modifier = Modifier.height(24.dp)) // Jarak di bagian bawah
    }
}
@Preview(showBackground = true)
@Composable
fun InfografikScreenPreview() {
    InfografikScreen()
}