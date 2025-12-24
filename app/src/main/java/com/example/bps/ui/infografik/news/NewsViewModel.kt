package com.example.bps.ui.infografik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.NewsItemResponse
import com.example.bps.data.remote.responses.PublicationItemResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State Umum untuk News (Berita, BRS, Infografik)
sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val news: List<NewsItemResponse>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

// State Khusus untuk Publikasi
sealed interface PublikasiUiState {
    object Loading : PublikasiUiState
    data class Success(val data: List<PublicationItemResponse>) : PublikasiUiState
    data class Error(val message: String) : PublikasiUiState
}

class NewsViewModel : ViewModel() {

    // 1. State untuk Berita Kegiatan
    private val _newsState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsState: StateFlow<NewsUiState> = _newsState.asStateFlow()

    // 2. State untuk Publikasi (Tab 1)
    private val _publicationState = MutableStateFlow<PublikasiUiState>(PublikasiUiState.Loading)
    val publicationState: StateFlow<PublikasiUiState> = _publicationState.asStateFlow()

    // 3. State untuk BRS (Tab 2)
    private val _brsState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val brsState: StateFlow<NewsUiState> = _brsState.asStateFlow()

    // 4. State untuk Infografik (Tab 3)
    private val _infografikState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val infografikState: StateFlow<NewsUiState> = _infografikState.asStateFlow()



    // 6. State untuk Refreshing (Pull-to-Refresh)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        fetchAllData()
    }

    // Fungsi fetch awal
    private fun fetchAllData() {
        viewModelScope.launch {
            fetchNews()
            fetchPublications()
            fetchBrs()
            fetchInfografik()
        }
    }

    // Fungsi Refresh (dijalankan saat user tarik layar)
    fun refreshAllData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                // Jalankan SEMUA request secara paralel
                val job1 = async { fetchNews() }
                val job2 = async { fetchPublications() }
                val job3 = async { fetchBrs() }
                val job4 = async { fetchInfografik() }

                // Tunggu kelimanya selesai
                awaitAll(job1, job2, job3, job4)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    // --- FUNGSI FETCH DATA ---

    // 1. Fetch Berita
    private suspend fun fetchNews() {
        try {
            if (!_isRefreshing.value) _newsState.value = NewsUiState.Loading
            val response = ApiClient.apiService.getNews()
            if (response.success) _newsState.value = NewsUiState.Success(response.data)
            else _newsState.value = NewsUiState.Error(response.message)
        } catch (e: Exception) {
            _newsState.value = NewsUiState.Error(e.message ?: "Error")
        }
    }

    // 2. Fetch Publikasi
    private suspend fun fetchPublications() {
        try {
            if (!_isRefreshing.value) _publicationState.value = PublikasiUiState.Loading
            val response = ApiClient.apiService.getPublications()
            if (response.success) _publicationState.value = PublikasiUiState.Success(response.data)
            else _publicationState.value = PublikasiUiState.Error(response.message)
        } catch (e: Exception) {
            _publicationState.value = PublikasiUiState.Error(e.message ?: "Error")
        }
    }

    // 3. Fetch BRS
    private suspend fun fetchBrs() {
        try {
            if (!_isRefreshing.value) _brsState.value = NewsUiState.Loading
            val response = ApiClient.apiService.getPressReleases()
            if (response.success) _brsState.value = NewsUiState.Success(response.data)
            else _brsState.value = NewsUiState.Error(response.message)
        } catch (e: Exception) {
            _brsState.value = NewsUiState.Error(e.message ?: "Error")
        }
    }

    // 4. Fetch Infografik
    private suspend fun fetchInfografik() {
        try {
            if (!_isRefreshing.value) _infografikState.value = NewsUiState.Loading
            val response = ApiClient.apiService.getInfographics()
            if (response.success) _infografikState.value = NewsUiState.Success(response.data)
            else _infografikState.value = NewsUiState.Error(response.message)
        } catch (e: Exception) {
            _infografikState.value = NewsUiState.Error(e.message ?: "Error")
        }
    }

    // --- HELPER UNTUK DETAIL SCREEN ---

    fun getPublicationById(id: Int): PublicationItemResponse? {
        val state = publicationState.value
        return if (state is PublikasiUiState.Success) {
            state.data.find { it.id == id }
        } else null
    }

    fun getNewsById(id: Int): NewsItemResponse? {
        val state = newsState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }

    fun getBrsById(id: Int): NewsItemResponse? {
        val state = brsState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }

    fun getInfografikById(id: Int): NewsItemResponse? {
        val state = infografikState.value
        return if (state is NewsUiState.Success) {
            state.news.find { it.id == id }
        } else null
    }
}