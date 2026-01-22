package com.example.bps.ui.statistik.datasetdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.DatasetDetailData
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = false,
    val dataset: DatasetDetailData? = null,
    val error: String? = null
)

class DetailDatasetViewModel : ViewModel() {
    private val _uiState = mutableStateOf(DetailUiState())
    val uiState: State<DetailUiState> = _uiState

    fun getDatasetDetail(id: Int, year: Int? = null, mode: String? = null) {
        viewModelScope.launch {
            // Set loading, tapi pertahankan data lama (jika ada) agar transisi smooth
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Panggil API (sekarang return-nya langsung DatasetDetailResponse)
                val response = ApiClient.apiService.getDatasetDetail(id, year, mode)

                // 2. Cek Boolean 'success' dari JSON
                if (response.success && response.data != null) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        dataset = response.data,
                        error = null
                    )

                } else {
                    // Jika success == false, ambil pesan error dari API
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = response.message ?: "Gagal memuat data"
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Terjadi kesalahan jaringan"
                )
            }
        }
    }
}
