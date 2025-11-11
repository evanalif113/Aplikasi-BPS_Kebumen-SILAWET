package com.example.bps.ui.lain

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.CarouselInsight
import com.example.bps.components.SearchBar

@Composable
fun LainScreen() {
    // Column sekarang bisa di-scroll untuk mengakomodasi lebih banyak konten
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CarouselInsight()
        Spacer(modifier = Modifier.height(24.dp))
        // Menambahkan SearchBar di sini
        SearchBar()
    }
}

@Preview(showBackground = true)
@Composable
fun LainScreenPreview() {
    LainScreen()
}