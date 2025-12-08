package com.example.bps.ui.statistik.SubjectList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.CategorySubjectResponse
import kotlinx.coroutines.launch
import java.lang.Exception

// State tidak perlu diubah
data class SubjectUiState(
    val isLoading: Boolean = false,
    val categoriesMap: List<CategorySubjectResponse> = emptyList(),
    val error: String? = null
)

class SubjectListViewModel : ViewModel() {
    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = SubjectUiState(
                isLoading = true
            )
            try {
                // Panggil API (Sekarang mengembalikan Wrapper Object)
                val response = ApiClient.apiService.getCategories()

                // Ambil list dari properti .data
                _uiState.value = SubjectUiState(
                    isLoading = false,
                    //categoriesMap = response.data // <-- Ini sekarang valid!
                )
            } catch (e: Exception) {
                _uiState.value = SubjectUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private val _uiState = mutableStateOf(SubjectUiState())
    val uiState: State<SubjectUiState> = _uiState

    init {
        loadCategories()
    }
}