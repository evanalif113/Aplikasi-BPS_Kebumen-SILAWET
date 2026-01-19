package com.example.bps.ui.statistik

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.DatasetResponse
import java.lang.Exception
import kotlinx.coroutines.launch

/**
 * Ini adalah State class KHUSUS untuk layar daftar. Mirip dengan DetailUiState, tapi 'dataset'
 * diganti 'datasets' (List)
 */
data class DatasetListUiState(
    val isLoading: Boolean = false,
    val datasets: List<DatasetResponse> = emptyList(), // Ini List, default-nya kosong
    val error: String? = null
)

/** ViewModel ini bertugas mengambil DAFTAR dataset berdasarkan filter (subject/kategori). */
class DatasetListViewModel : ViewModel() {

    private val _uiState = mutableStateOf(DatasetListUiState())
    val uiState: State<DatasetListUiState> = _uiState

    fun getDatasetList(subject: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response = ApiClient.apiService.getDatasetList(subject)

                _uiState.value = DatasetListUiState(isLoading = false, datasets = response.data)
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
