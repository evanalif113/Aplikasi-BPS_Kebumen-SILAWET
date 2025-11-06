package com.example.bps.ui.datasetdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.BpsDatasetResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DetailUiState(
        val isLoading: Boolean = false,
        val dataset: BpsDatasetResponse? = null,
        val error: String? = null
)

class DetailDatasetViewModel : ViewModel() {

    private val _uiState = mutableStateOf(DetailUiState())
    val uiState: State<DetailUiState> = _uiState

    fun getDatasetDetail(datasetId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val response =
                        withContext(Dispatchers.IO) {
                            ApiClient.apiService.getDatasetDetail(datasetId)
                        }
                _uiState.value =
                        _uiState.value.copy(isLoading = false, dataset = response, error = null)
            } catch (e: Exception) {
                _uiState.value =
                        _uiState.value.copy(
                                isLoading = false,
                                dataset = null,
                                error = e.message ?: "Unknown error"
                        )
            }
        }
    }
}
