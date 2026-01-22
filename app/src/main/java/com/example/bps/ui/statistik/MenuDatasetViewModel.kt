package com.example.bps.ui.statistik

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.GridDatasetItem
import kotlinx.coroutines.launch

data class MenuDatasetUiState(
    val isLoading: Boolean = false,
    val datasets: List<GridDatasetItem> = emptyList(),
    val error: String? = null
)

class MenuDatasetViewModel : ViewModel() {
    private val _uiState = mutableStateOf(MenuDatasetUiState())
    val uiState: State<MenuDatasetUiState> = _uiState

    fun getDatasetsBySlug(slug: String) {
        viewModelScope.launch {
            _uiState.value = MenuDatasetUiState(isLoading = true)
            try {
                val response = ApiClient.apiService.getGridByCategory(slug)

                // PERUBAHAN 1: Cek boolean 'success'
                if (response.success) {

                    // PERUBAHAN 2: Akses data lewat wrapper '.data'
                    // Kita gunakan elvis operator (?:) untuk jaga-jaga jika null
                    val datasetList = response.data?.datasets ?: emptyList()

                    _uiState.value = MenuDatasetUiState(
                        isLoading = false,
                        datasets = datasetList
                    )

                    // Tips: Jika Anda butuh nama kategori yang dinamis dari API:
                    // val categoryName = response.data?.category
                } else {
                    _uiState.value = MenuDatasetUiState(
                        isLoading = false,
                        // Ambil pesan error dari response jika ada
                        error = response.message ?: "Gagal memuat data"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = MenuDatasetUiState(
                    isLoading = false,
                    error = e.message ?: "Terjadi kesalahan"
                )
            }
        }
    }
}