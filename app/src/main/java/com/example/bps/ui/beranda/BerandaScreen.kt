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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bps.components.* // Import komponen kita
import com.example.bps.data.remote.responses.IndicatorItem
import com.example.bps.data.remote.responses.NewsItemResponse
import com.example.bps.data.remote.responses.PublicationItemResponse
import com.example.bps.theme.BpsTheme
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

    BerandaContent(
        indicatorList = indicatorList,
        isIndicatorLoading = isIndicatorLoading,
        isRefreshing = isNewsRefreshing || isIndicatorLoading,
        onRefresh = {
            newsViewModel.refreshAllData()
            indicatorViewModel.getIndicators()
        },
        newsState = newsState,
        publicationList = if (publicationState is PublikasiUiState.Success) (publicationState as PublikasiUiState.Success).data else emptyList(),
        brsList = if (brsState is NewsUiState.Success) (brsState as NewsUiState.Success).news else emptyList(),
        infografikList = if (infografikState is NewsUiState.Success) (infografikState as NewsUiState.Success).news else emptyList(),
        onSeeAllNews = onSeeAllNews,
        onNavigateToDetail = onNavigateToDetail,
        onMenuClick = onMenuClick,
        onSearchClick = onSearchClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaContent(
    indicatorList: List<IndicatorItem>,
    isIndicatorLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    newsState: NewsUiState,
    publicationList: List<PublicationItemResponse>,
    brsList: List<NewsItemResponse>,
    infografikList: List<NewsItemResponse>,
    onSeeAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit,
    onMenuClick: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
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
                publicationList = publicationList,
                brsList = brsList,
                infografikList = infografikList,
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

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BpsTheme {
        BerandaContent(
            indicatorList = listOf(
                IndicatorItem(
                    slug = "inflasi",
                    categoryTitle = "Inflasi",
                    displayName = "Tingkat Inflasi",
                    datasetId = 1,
                    datasetCode = "123",
                    datasetName = "Inflasi Bulanan",
                    value = "2.5",
                    year = 2024,
                    unit = "%"
                ),
                IndicatorItem(
                    slug = "kemiskinan",
                    categoryTitle = "Kemiskinan",
                    displayName = "Persentase Penduduk Miskin",
                    datasetId = 2,
                    datasetCode = "456",
                    datasetName = "Kemiskinan Tahunan",
                    value = "10.2",
                    year = 2023,
                    unit = "%"
                )
            ),
            isIndicatorLoading = false,
            isRefreshing = false,
            onRefresh = {},
            newsState = NewsUiState.Success(
                listOf(
                    NewsItemResponse(
                        id = 1,
                        title = "BPS Kebumen Melakukan Sosialisasi Sensus Pertanian 2024",
                        date = "2024-05-20",
                        thumbnailUrl = "https://statik.unesa.ac.id/terapan-ti/thumbnail/9a532a94-65b4-4071-8478-31ea69fccb74.png",
                        abstract = "Sosialisasi ini dilakukan untuk memberikan pemahaman kepada masyarakat..."
                    )
                )
            ),
            publicationList = listOf(
                PublicationItemResponse(
                    id = 1,
                    title = "Kabupaten Kebumen Dalam Angka 2024",
                    releaseDate = "2024-02-28",
                    coverUrl = "https://via.placeholder.com/150",
                    pdfUrl = "https://example.com/pdf",
                    abstract = "Publikasi ini menyajikan data statistik kabupaten kebumen..."
                )
            ),
            brsList = listOf(
                NewsItemResponse(
                    id = 1,
                    title = "Perkembangan Indeks Harga Konsumen Mei 2024",
                    date = "2024-06-01",
                    thumbnailUrl = "https://statik.unesa.ac.id/terapan-ti/thumbnail/9a532a94-65b4-4071-8478-31ea69fccb74.png"
                )
            ),
            infografikList = listOf(
                NewsItemResponse(
                    id = 1,
                    title = "Infografis Inflasi Mei 2024",
                    date = "2024-06-01",
                    thumbnailUrl = "https://via.placeholder.com/150"
                )
            ),
            onSeeAllNews = {},
            onNavigateToDetail = { _, _ -> },
            onMenuClick = {},
            onSearchClick = {}
        )
    }
}
