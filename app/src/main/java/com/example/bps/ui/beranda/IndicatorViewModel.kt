package com.example.bps.ui.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.IndicatorItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IndicatorViewModel : ViewModel() {

    private val _indicatorState = MutableStateFlow<List<IndicatorItem>>(emptyList())
    val indicatorState: StateFlow<List<IndicatorItem>> = _indicatorState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getIndicators()
    }

    fun getIndicators() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiClient.apiService.getStrategicIndicators()

                if (response.success) {
                    _indicatorState.value = response.data

                    // Opsional: Jika butuh meta (total/timestamp), bisa diambil di sini:
                    // val total = response.meta?.totalIndicators
                    // val time = response.meta?.timestamp
                } else {
                    // Log.e("API", response.message ?: "Unknown Error")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
