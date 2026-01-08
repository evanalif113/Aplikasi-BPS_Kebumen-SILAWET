package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // PENTING: Import ini untuk kata kunci 'by'
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
    // Ubah nama parameter agar tidak bingung
    newsViewModel: NewsViewModel,
    // Tambahkan parameter ViewModel baru ini
    indicatorViewModel: IndicatorViewModel = viewModel(),
    onSeeAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit,
    onMenuClick: (String) -> Unit
) {
    // 1. Ambil State Indikator dari 'indicatorViewModel' (BUKAN newsViewModel)
    val indicatorList by indicatorViewModel.indicatorState.collectAsState()

    // 2. Ambil State Berita dari 'newsViewModel'
    val newsState by newsViewModel.newsState.collectAsState()
    val publicationState by newsViewModel.publicationState.collectAsState()
    val brsState by newsViewModel.brsState.collectAsState()
    val infografikState by newsViewModel.infografikState.collectAsState()

    // Gabungkan status loading
    val isNewsRefreshing by newsViewModel.isRefreshing.collectAsState()
    val isIndicatorLoading by indicatorViewModel.isLoading.collectAsState()
    val isRefreshing = isNewsRefreshing || isIndicatorLoading

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            newsViewModel.refreshAllData()
            indicatorViewModel.getIndicators() // Refresh data indikator juga
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            // Pasang Data Indikator ke Carousel
            CarouselInsight(
                indicators = indicatorList,
                onItemClick = { /* Handle klik */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SearchBar()
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