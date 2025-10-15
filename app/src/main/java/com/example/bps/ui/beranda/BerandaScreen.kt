package com.example.bps.ui.beranda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.components.MenuItemSection
import com.example.bps.components.SearchBar
import com.example.bps.components.CarouselInsight
import com.example.bps.components.InfografikSection
import com.example.bps.components.InfoSensusSection

@Composable
fun BerandaScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        // CardInsight sekarang memiliki padding horizontal sendiri
        CarouselInsight()

        Spacer(modifier = Modifier.height(24.dp))

        // SearchBar di bagian atas
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Menu Utama (Infografi, Statistik, Lainnya)
        MenuItemSection()
        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Info Sensus
        InfoSensusSection()
        Spacer(modifier = Modifier.height(24.dp))
        InfografikSection()
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen()
}