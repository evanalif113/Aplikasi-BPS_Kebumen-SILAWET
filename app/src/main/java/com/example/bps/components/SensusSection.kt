package com.example.bps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bps.R

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

@Preview
@Composable
fun InfoSensusSectionPreview() {
    InfoSensusSection()
}