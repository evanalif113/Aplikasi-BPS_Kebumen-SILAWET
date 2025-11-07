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
    fun getDatasetList(subject: String) {
        viewModelScope.launch {
            _uiState.value = DatasetListUiState(isLoading = true)
            try {
                // Pastikan memanggil API pakai 'subject'
                val response = ApiClient.apiService.getDatasetList(subject = subject)
                _uiState.value = DatasetListUiState(isLoading = false, datasets = response)
            } catch (e: Exception) {
                _uiState.value =
                        DatasetListUiState(
                                isLoading = false,
                                error = e.message ?: "Terjadi kesalahan"
                        )
            }
        }
    }
}
