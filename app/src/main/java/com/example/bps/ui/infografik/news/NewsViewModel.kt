package com.example.bps.ui.infografik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.data.remote.responses.PublicationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State Umum untuk News (Berita, BRS, Infografik)
sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val news: List<NewsItem>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

// State Khusus untuk Publikasi (Karena tipe datanya beda)
sealed interface PublicationUiState {
    object Loading : PublicationUiState
    data class Success(val data: List<PublicationItem>) : PublicationUiState
    data class Error(val message: String) : PublicationUiState
}

class NewsViewModel : ViewModel() {

    // 1. State untuk Berita Kegiatan (Yang sudah ada)
    private val _newsState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsState: StateFlow<NewsUiState> = _newsState.asStateFlow()

    // 2. State untuk Publikasi (Tab 1)
    private val _publicationState = MutableStateFlow<PublicationUiState>(PublicationUiState.Loading)
    val publicationState: StateFlow<PublicationUiState> = _publicationState.asStateFlow()

    // 3. State untuk BRS (Tab 2)
    private val _brsState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val brsState: StateFlow<NewsUiState> = _brsState.asStateFlow()

    // 4. State untuk Infografik (Tab 3)
    private val _infografikState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val infografikState: StateFlow<NewsUiState> = _infografikState.asStateFlow()

    init {
        fetchAllData()
    }

    private fun fetchAllData() {
        fetchNews()        // Berita Kegiatan
        fetchPublications() // Tab 1
        fetchBrs()         // Tab 2
        fetchInfografik()  // Tab 3
    }

    private fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getNews()
                if (response.success) _newsState.value = NewsUiState.Success(response.data)
                else _newsState.value = NewsUiState.Error(response.message)
            } catch (e: Exception) { _newsState.value = NewsUiState.Error(e.message ?: "Error") }
        }
    }

    private fun fetchPublications() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPublications()
                if (response.success) _publicationState.value = PublicationUiState.Success(response.data)
                else _publicationState.value = PublicationUiState.Error(response.message)
            } catch (e: Exception) { _publicationState.value = PublicationUiState.Error(e.message ?: "Error") }
        }
    }

    private fun fetchBrs() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getPressReleases()
                if (response.success) _brsState.value = NewsUiState.Success(response.data)
                else _brsState.value = NewsUiState.Error(response.message)
            } catch (e: Exception) { _brsState.value = NewsUiState.Error(e.message ?: "Error") }
        }
    }

    private fun fetchInfografik() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getInfographics()
                if (response.success) _infografikState.value = NewsUiState.Success(response.data)
                else _infografikState.value = NewsUiState.Error(response.message)
            } catch (e: Exception) { _infografikState.value = NewsUiState.Error(e.message ?: "Error") }
        }
    }

    // --- TAMBAHAN PENTING UNTUK DETAIL SCREEN ---

    // 1. Cari Publikasi (Buku) berdasarkan ID
    fun getPublicationById(id: Int): PublicationItem? {
        val state = publicationState.value
        // Cek apakah state sedang Success
        return if (state is PublicationUiState.Success) {
            // Cari item yang ID-nya cocok
            state.data.find { it.id == id }
        } else null
    }

    // 2. Cari Berita / BRS / Infografik berdasarkan ID
    // Karena model datanya sama (NewsItem), kita cari di ketiga list sekaligus
    // 1. Khusus cari di list Berita Kegiatan
    fun getNewsActivityById(id: Int): NewsItem? {
        val state = newsState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }

    // 2. Khusus cari di list BRS
    fun getBrsById(id: Int): NewsItem? {
        val state = brsState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }

    // 3. Khusus cari di list Infografik
    fun getInfografikById(id: Int): NewsItem? {
        val state = infografikState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }
}