package com.example.bps.ui.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.SearchBar
import com.example.bps.theme.*

@Composable
fun MapsScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- LAYER 1: PETA (DI BAGIAN BAWAH) ---
        // Ini adalah placeholder. Nantinya, komponen Google Maps akan ditaruh di sini.
        MapPlaceholder(modifier = Modifier.fillMaxSize())

        // --- LAYER 2: UI KONTROL (DI BAGIAN ATAS) ---
        // Kolom ini digunakan untuk menempatkan SearchBar di bagian atas layar
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar Anda
            SearchBar()
        }
    }
}

/**
 * Composable placeholder untuk area peta.
 */
@Composable
fun MapPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp), // Peta biasanya tidak memiliki sudut bulat
        colors = CardDefaults.cardColors(containerColor = Gray200)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Area Peta Akan Tampil di Sini",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Gray500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapsScreenPreview() {
    MapsScreen()
}