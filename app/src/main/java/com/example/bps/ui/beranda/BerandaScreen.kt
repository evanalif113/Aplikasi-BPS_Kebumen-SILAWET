package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bps.components.MenuItemSection
import com.example.bps.components.SearchBar
import com.example.bps.components.CarouselInsight
import com.example.bps.components.NewsSection
import com.example.bps.components.InfoSensusSection
import com.example.bps.components.TabbedContentSection
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.infografik.news.NewsUiState
import com.example.bps.ui.infografik.news.PublicationUiState
import com.example.bps.ui.common.ContentType

@Composable
fun BerandaScreen(
    viewModel: NewsViewModel,
    onSeeAllNews: () -> Unit,
    onNavigateToDetail: (Int, ContentType) -> Unit
) {
    // 1. AMBIL SEMUA STATE DARI VIEWMODEL (Update Baru)
    val newsState by viewModel.newsState.collectAsState()
    val publicationState by viewModel.publicationState.collectAsState()
    val brsState by viewModel.brsState.collectAsState()
    val infografikState by viewModel.infografikState.collectAsState()

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
        Spacer(modifier = Modifier.height(36.dp))

        TabbedContentSection(
            publicationList = if (publicationState is PublicationUiState.Success)
                (publicationState as PublicationUiState.Success).data
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

        Spacer(modifier = Modifier.height(24.dp))

        // 3. NEWS SECTION (Berita Kegiatan)
        NewsSection(
            uiState = newsState, // <-- Gunakan 'newsState' yang baru
            onSeeAllClicked = onSeeAllNews
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(
        viewModel = viewModel(),
        onSeeAllNews = {},
        onNavigateToDetail = { _, _ -> }
    )
}