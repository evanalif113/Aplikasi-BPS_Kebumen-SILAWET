package com.example.bps.ui.statistik.datasetdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.BpsDatasetClass
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = false,
    val dataset: BpsDatasetClass? = null,
    val error: String? = null
)

class DetailDatasetViewModel : ViewModel() {
    private val _uiState = mutableStateOf(DetailUiState())
    val uiState: State<DetailUiState> = _uiState

    fun getDatasetDetail(id: String, year: Int? = null, mode: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true) // Jangan reset data lama biar smooth
            try {
                // Kirim mode ke API
                val response = ApiClient.apiService.getDatasetDetail(id, year, mode)

                if (response.isSuccessful && response.body() != null) {
                    val newData = response.body()!!

                    // PENTING: Jika user cuma ganti MODE, jangan reset available_years
                    // Kita gabungkan data lama & baru
                    _uiState.value = DetailUiState(
                        isLoading = false,
                        dataset = newData
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Gagal")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
