package com.example.bps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext // <-- Ganti UriHandler dengan ini
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.utils.launchInAppBrowser // <-- Import fungsi helper tadi

@Composable
fun SensusBanner(
    imageRes: Int,
    backgroundColor: Color,
    textColor: Color,
    title: String,
    url: String
) {
    // 1. Ambil Context saat ini
    val context = LocalContext.current

    Card(
        // 2. Ubah onClick untuk memanggil In-App Browser
        onClick = {
            launchInAppBrowser(context, url)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // --- 1. DEKORASI LATAR BELAKANG ---
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                alpha = 0.35f,
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 30.dp, y = 30.dp)
                    .rotate(-15f)
            )

            // --- 2. KONTEN UTAMA ---
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // IKON UTAMA
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // TEKS JUDUL
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun InfoSensusSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Info Sensus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )

        // 1. SENSUS PENDUDUK
        SensusBanner(
            imageRes = R.drawable.banner_sp2020,
            backgroundColor = Color(0xFFD6F3FF),
            textColor = Color(0xFF006898),
            title = "SENSUS\nPENDUDUK 2020",
            url = "https://sensus.bps.go.id/main/index/sp2020"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. SENSUS PERTANIAN
        SensusBanner(
            imageRes = R.drawable.banner_st2023,
            backgroundColor = Color(0xFFD8F5CD),
            textColor = Color(0xFF2E7D32),
            title = "SENSUS\nPERTANIAN 2023",
            url = "https://sensus.bps.go.id/main/index/st2023"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. SENSUS EKONOMI
        SensusBanner(
            imageRes = R.drawable.banner_se2026_2,
            backgroundColor = Color(0xFFFFEAB6),
            textColor = Color(0xFFB56C00),
            title = "SENSUS\nEKONOMI 2026",
            url = "https://sensus.bps.go.id/se2026/"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InfoSensusSectionPreview() {
    InfoSensusSection()
}