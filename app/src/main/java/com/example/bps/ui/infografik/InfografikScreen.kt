package com.example.bps.ui.infografik

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.InfografikSection
import com.example.bps.components.SearchBar

@Composable
fun InfografikScreen() {
    // Column sekarang bisa di-scroll untuk mengakomodasi lebih banyak konten
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))
        InfografikSection()
    }
}

@Preview(showBackground = true)
@Composable
fun InfografikScreenPreview() {
    InfografikScreen()
}