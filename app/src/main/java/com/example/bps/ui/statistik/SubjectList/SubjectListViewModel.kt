package com.example.bps.ui.statistik.SubjectList

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
    val categoriesMap: List<CategorySubjectResponse> = emptyList(),
    val error: String? = null
)

class SubjectListViewModel : ViewModel() {
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = SubjectUiState(isLoading = true)
            try {
                // Response sekarang berupa Map<String, CategorySubjectResponse>
                val responseMap = ApiClient.apiService.getCategories()

                // Konversi Map values menjadi List
                val categoriesList = responseMap.values.toList()

                _uiState.value = SubjectUiState(
                    isLoading = false,
                    categoriesMap = categoriesList // Masukkan List hasil konversi
                )
            } catch (e: Exception) {
                _uiState.value = SubjectUiState(isLoading = false, error = e.message)
            }
        }
    }

    private val _uiState = mutableStateOf(SubjectUiState())
    val uiState: State<SubjectUiState> = _uiState

    init {
        loadCategories()
    }
}