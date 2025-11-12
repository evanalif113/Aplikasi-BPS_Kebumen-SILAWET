package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.MenuItemSection
import com.example.bps.components.SearchBar
import com.example.bps.components.CarouselInsight
import com.example.bps.components.NewsSection
import com.example.bps.components.InfoSensusSection
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bps.ui.infografik.news.NewsViewModel

@Composable
fun BerandaScreen(
    // 1. Terima ViewModel dan event click
    viewModel: NewsViewModel,
    onSeeAllNews: () -> Unit
) {
    // 2. Ambil state dari ViewModel
    val newsUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        CarouselInsight()
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))
        MenuItemSection()
        Spacer(modifier = Modifier.height(24.dp))
        InfoSensusSection()
        Spacer(modifier = Modifier.height(24.dp))

        // 3. Teruskan state dan click handler ke Section yang relevan
        NewsSection(
            uiState = newsUiState, // <-- Teruskan state
            onSeeAllClicked = onSeeAllNews // <-- Teruskan fungsi klik
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BerandaScreenPreview() {

    BerandaScreen(
        viewModel = viewModel(), // Cara mudah untuk Preview
        onSeeAllNews = {} // Fungsi kosong untuk preview
    )
}