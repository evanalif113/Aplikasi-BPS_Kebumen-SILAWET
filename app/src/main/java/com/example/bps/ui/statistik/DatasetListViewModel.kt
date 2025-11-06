package com.example.bps.ui.statistik

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.SimpleDatasetResponse
import java.lang.Exception
import kotlinx.coroutines.launch

/**
 * Ini adalah State class KHUSUS untuk layar daftar. Mirip dengan DetailUiState, tapi 'dataset'
 * diganti 'datasets' (List)
 */
data class DatasetListUiState(
        val isLoading: Boolean = false,
        val datasets: List<SimpleDatasetResponse> = emptyList(), // Ini List, default-nya kosong
        val error: String? = null
)

/** ViewModel ini bertugas mengambil DAFTAR dataset berdasarkan filter (subject/kategori). */
class DatasetListViewModel : ViewModel() {

    private val _uiState = mutableStateOf(DatasetListUiState())
    val uiState: State<DatasetListUiState> = _uiState

    /** @param subject Filter berdasarkan subject (misal: "Ekonomi") */
    fun getDatasetList(categoryId: String) {
        viewModelScope.launch {
            _uiState.value = DatasetListUiState(isLoading = true)

            try {
                // Ubah String "2" menjadi Int 2
                val idAsInt = categoryId.toIntOrNull()

                // Panggil API menggunakan 'category' (Int)
                val response = ApiClient.apiService.getDatasetList(category = idAsInt)

                _uiState.value = DatasetListUiState(isLoading = false, datasets = response)

            } catch (e: Exception) {
                // 4. Gagal: update state dengan pesan error
                _uiState.value =
                        DatasetListUiState(
                                isLoading = false,
                                error = e.message ?: "Terjadi kesalahan"
                        )
            }
        }
    }
}
