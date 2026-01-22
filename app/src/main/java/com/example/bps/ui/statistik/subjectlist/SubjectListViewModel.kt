package com.example.bps.ui.statistik.subjectlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.CategorySubjectResponse
import kotlinx.coroutines.launch
import java.lang.Exception

// State tetap menggunakan List agar mudah dibaca UI
data class SubjectUiState(
    val isLoading: Boolean = false,
    val categories: List<CategorySubjectResponse> = emptyList(),
    val error: String? = null
)

class SubjectListViewModel : ViewModel() {

    // State UI
    private val _uiState = mutableStateOf(SubjectUiState())
    val uiState: State<SubjectUiState> = _uiState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = SubjectUiState(isLoading = true)
            try {
                // 1. Panggil API (Dapatnya CategoryResponse, bukan Map langsung)
                val responseWrapper = ApiClient.apiService.getCategories()

                // 2. Cek apakah success DAN datanya ada
                if (responseWrapper.success && responseWrapper.data != null) {

                    // 3. Ambil Map dari .data, lalu ambil .values, lalu jadikan List
                    val categoriesList = responseWrapper.data.values.toList()

                    _uiState.value = SubjectUiState(
                        isLoading = false,
                        // Pastikan di data class SubjectUiState tipe datanya sudah List
                        categories = categoriesList
                    )
                } else {
                    _uiState.value = SubjectUiState(
                        isLoading = false,
                        error = responseWrapper.message ?: "Data kosong"
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = SubjectUiState(isLoading = false, error = e.message)
            }
        }
    }
}