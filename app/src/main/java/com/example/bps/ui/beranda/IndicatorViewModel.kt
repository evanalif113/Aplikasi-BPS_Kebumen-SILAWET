package com.example.bps.ui.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bps.data.remote.ApiClient
import com.example.bps.data.remote.responses.IndicatorItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IndicatorViewModel : ViewModel() {

    // State untuk menampung list indikator
    private val _indicatorState = MutableStateFlow<List<IndicatorItem>>(emptyList())
    val indicatorState: StateFlow<List<IndicatorItem>> = _indicatorState

    // State loading (opsional, untuk menampilkan progress bar)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Otomatis ambil data saat ViewModel dibuat
        getIndicators()
    }

    fun getIndicators() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Panggil API
                val response = ApiClient.apiService.getStrategicIndicators()

                // Jika sukses, simpan data ke state
                if (response.status == "success") {
                    _indicatorState.value = response.data
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log error jika gagal (misal tidak ada internet)
            } finally {
                _isLoading.value = false
            }
        }
    }
}