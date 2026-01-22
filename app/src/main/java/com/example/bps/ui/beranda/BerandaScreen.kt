package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bps.components.* // Import komponen kita
import com.example.bps.ui.general.ContentType
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.infografik.news.PublikasiUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    newsViewModel: NewsViewModel,
    indicatorViewModel: IndicatorViewModel = viewModel(),
    onSeeAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit,
    onMenuClick: (String) -> Unit,
    onSearchClick: () -> Unit // <--- WAJIB ADA INI
) {
    val indicatorList by indicatorViewModel.indicatorState.collectAsState()
    val isIndicatorLoading by indicatorViewModel.isLoading.collectAsState()
    val isNewsRefreshing by newsViewModel.isRefreshing.collectAsState()

    // State Berita
    val newsState by newsViewModel.newsState.collectAsState()
    val publicationState by newsViewModel.publicationState.collectAsState()
    val brsState by newsViewModel.brsState.collectAsState()
    val infografikState by newsViewModel.infografikState.collectAsState()

    PullToRefreshBox(
        isRefreshing = isNewsRefreshing || isIndicatorLoading,
        onRefresh = {
            newsViewModel.refreshAllData()
            indicatorViewModel.getIndicators()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            CarouselInsight(
                indicators = indicatorList,
                isLoading = isIndicatorLoading,
                onItemClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- GANTI SearchBar() DENGAN HomeSearchBar ---
            HomeSearchBar(
                onSearchClicked = onSearchClick // Panggil fungsi navigasi
            )

            Spacer(modifier = Modifier.height(20.dp))
            MenuItemSection(onItemClick = onMenuClick)
            Spacer(modifier = Modifier.height(20.dp))

            TabbedContentSection(
                publicationList = if (publicationState is PublikasiUiState.Success) (publicationState as PublikasiUiState.Success).data else emptyList(),
                brsList = if (brsState is NewsUiState.Success) (brsState as NewsUiState.Success).news else emptyList(),
                infografikList = if (infografikState is NewsUiState.Success) (infografikState as NewsUiState.Success).news else emptyList(),
                onItemClick = { id, type -> onNavigateToDetail(id, type) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            NewsSection(
                uiState = newsState,
                onSeeAllClicked = onSeeAllNews,
                onItemClicked = { id -> onNavigateToDetail(id, ContentType.NEWS) }
            )

            InfoSensusSection()
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}