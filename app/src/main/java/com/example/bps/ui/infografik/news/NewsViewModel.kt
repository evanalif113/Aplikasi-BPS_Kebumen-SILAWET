package com.example.bps.ui.infografik.news // Ganti package ini sesuai struktur proyek Anda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient // <-- Pastikan import ini benar
import com.example.bps.data.remote.responses.NewsItem
import com.example.bps.data.remote.responses.BpsNewsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Definisikan state untuk UI Anda
sealed interface NewsUiState {
    object Loading : NewsUiState
    data class Success(val news: List<NewsItem>) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

class NewsViewModel : ViewModel() {

    // 2. Buat StateFlow untuk menampung state
    //    _uiState bersifat privat agar hanya ViewModel yang bisa mengubahnya
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    //    uiState bersifat publik agar UI (Composable) bisa membacanya
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        // 3. Langsung panggil API saat ViewModel ini pertama kali dibuat
        fetchNews()
    }

    fun fetchNews() {
        _uiState.value = NewsUiState.Loading
        viewModelScope.launch {
            try {
                // Sekarang 'response' akan dikenali sebagai 'BpsNewsResponse'
                val response = ApiClient.apiService.getNews()

                if (response.success) {
                    // 'response.data' adalah List<NewsItem>
                    _uiState.value = NewsUiState.Success(response.data) // <-- Error akan hilang
                } else {
                    _uiState.value = NewsUiState.Error(response.message) // <-- Error akan hilang
                }
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error("Gagal memuat data: ${e.message}")
            }
        }
    }
}