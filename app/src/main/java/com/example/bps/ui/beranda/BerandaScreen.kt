package com.example.bps.ui.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.components.SearchBar
import com.example.bps.components.CardInsight
import com.example.bps.components.NewsSection
import com.example.bps.theme.*

// 1. Tampilan utama BerandaScreen
@Composable
fun BerandaScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        // Carousel Section menggunakan CardCarousel
        CardInsight()
        Spacer(modifier = Modifier.height(24.dp))

        // SearchBar di bagian atas
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Menu Utama (Infografi, Statistik, Lainnya)
        MainMenuSection()
        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Info Sensus
        InfoSensusSection()
        Spacer(modifier = Modifier.height(24.dp))

        NewsSection()
    }
}

// 3. Composable untuk bagian Menu Utama
@Composable
fun MainMenuSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainMenuItem(iconRes = R.drawable.ic_maps_24dp, title = "Peta", colorCard = Blue400)
            MainMenuItem(iconRes = R.drawable.ic_grafik_24dp, title = "Statistik", colorCard = Orange300)
            MainMenuItem(iconRes = R.drawable.ic_open_book_24dp, title = "Infografik", colorCard = Red300)
            MainMenuItem(iconRes = R.drawable.ic_menu_24dp, title = "Lainnya", colorCard = Green300)
        }
    }
}

// 4. Composable untuk setiap item di dalam Menu Utama
@Composable
fun MainMenuItem(iconRes: Int, title: String, colorCard: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(72.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = Black,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            lineHeight = 14.sp
        )
    }
}

// 5. Composable untuk bagian Info Sensus
@Composable
fun InfoSensusSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Info Sensus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))
        SensusBanner(
            imageRes = R.drawable.banner_sp2020,
            backgroundColor = Color(0xFFA0E7F8),
            contentDescription = "Sensus Penduduk 2020"
        )

        Spacer(modifier = Modifier.height(12.dp))
        SensusBanner(
            imageRes = R.drawable.banner_st2023,
            backgroundColor = Color(0xFFC7EEA5),
            contentDescription = "Sensus Pertanian 2023"
        )

        Spacer(modifier = Modifier.height(12.dp))
        SensusBanner(
            imageRes = R.drawable.banner_se2026_2,
            backgroundColor = Color(0xFFFDE09A),
            contentDescription = "Sensus Ekonomi 2026"
        )
    }
}

// 6. Composable untuk setiap banner Sensus
@Composable
fun SensusBanner(imageRes: Int, backgroundColor: Color, contentDescription: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Menambahkan padding agar background terlihat
            contentDescription = contentDescription
        )
    }
}

@Composable
fun NewsInsightsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    BerandaScreen()
}