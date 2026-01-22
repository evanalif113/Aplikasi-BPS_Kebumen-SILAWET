package com.example.bps.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.DatasetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val data: List<DatasetResponse>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class SearchViewModel : ViewModel() {
    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState

    fun searchDatasets(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            try {
                // Memanggil endpoint: api/datasets?q={query}
                val response = ApiClient.apiService.getDatasetList(searchQuery = query)
                if (response.success) {
                    // Akses bersarang: response -> data (pagination) -> data (list)
                    _searchState.value = SearchUiState.Success(response.pagination.datasets)
                } else {
                    _searchState.value = SearchUiState.Error("Gagal mengambil data")
                }
            } catch (e: Exception) {
                _searchState.value = SearchUiState.Error("Error: ${e.message}")
            }
        }
    }
}