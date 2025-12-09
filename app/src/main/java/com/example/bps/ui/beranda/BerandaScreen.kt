package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox // Wajib Material3 ver 1.3.0+
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bps.components.CarouselInsight
import com.example.bps.components.InfoSensusSection
import com.example.bps.components.MenuItemSection
import com.example.bps.components.NewsSection
import com.example.bps.components.SearchBar
import com.example.bps.components.TabbedContentSection
import com.example.bps.ui.general.ContentType
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.infografik.news.PublikasiUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    viewModel: NewsViewModel,
    onSeeAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit,
    onMenuClick: (String) -> Unit // Tambahan: Callback untuk navigasi menu
) {
    // 1. Ambil State Data
    val newsState by viewModel.newsState.collectAsState()
    val publicationState by viewModel.publicationState.collectAsState()
    val brsState by viewModel.brsState.collectAsState()
    val infografikState by viewModel.infografikState.collectAsState()

    // 2. Ambil State Refreshing (Loading saat ditarik)
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // 3. Bungkus seluruh konten dengan PullToRefreshBox
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refreshAllData() // Panggil fungsi refresh di ViewModel
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Penting agar bisa discroll/ditarik meskipun konten sedikit
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            CarouselInsight()
            Spacer(modifier = Modifier.height(24.dp))

            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))

            // Hubungkan callback navigasi ke MenuItemSection
            MenuItemSection(
            )

            Spacer(modifier = Modifier.height(20.dp))

            InfoSensusSection()
            Spacer(modifier = Modifier.height(30.dp))

            TabbedContentSection(
                publicationList = if (publicationState is PublikasiUiState.Success)
                    (publicationState as PublikasiUiState.Success).data
                else emptyList(),

                brsList = if (brsState is NewsUiState.Success)
                    (brsState as NewsUiState.Success).news
                else emptyList(),

                infografikList = if (infografikState is NewsUiState.Success)
                    (infografikState as NewsUiState.Success).news
                else emptyList(),

                onItemClick = { id, type ->
                    onNavigateToDetail(id, type)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // News Section (Berita Kegiatan)
            NewsSection(
                uiState = newsState,
                onSeeAllClicked = onSeeAllNews
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(
        viewModel = viewModel(),
        onSeeAllNews = {},
        onNavigateToDetail = { _, _ -> },
        onMenuClick = {}
    )
}