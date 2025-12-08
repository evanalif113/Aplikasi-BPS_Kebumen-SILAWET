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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R // Pastikan import R sesuai package Anda

@Composable
fun SensusBanner(
    imageRes: Int,
    backgroundColor: Color,
    textColor: Color, // Tambahan: Warna teks agar kontras
    title: String,    // Judul banner (misal: "Sensus Penduduk 2020")
    url: String
) {
    val uriHandler = LocalUriHandler.current

    Card(
        onClick = { uriHandler.openUri(url) },
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp), // Sedikit lebih tinggi biar lega
        shape = RoundedCornerShape(16.dp), // Sudut lebih membulat
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Flat design (modern)
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // Warna dihandle di Box
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // --- 1. DEKORASI LATAR BELAKANG (Efek Watermark) ---
            // Ini membuat banner tidak terlihat "polos"
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                alpha = 0.35f, // Transparan (samar-samar)
                modifier = Modifier
                    .size(180.dp) // Perbesar gambar
                    .align(Alignment.BottomEnd) // Taruh di pojok kanan bawah
                    .offset(x = 30.dp, y = 30.dp) // Geser sedikit keluar
                    .rotate(-15f) // Miringkan sedikit
            )

            // --- 2. KONTEN UTAMA (Row: Ikon + Teks) ---
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp), // Jarak kiri-kanan
                verticalAlignment = Alignment.CenterVertically
            ) {
                // IKON UTAMA (Kiri)
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp) // Ukuran logo utama
                    // Jika logo Anda warna putih/hitam dan mau diwarnai sesuai tema, pakai:
                    // colorFilter = ColorFilter.tint(textColor)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // TEKS JUDUL (Kanan)
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor, // Warna teks disesuaikan (misal Biru Tua di atas Biru Muda)
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun InfoSensusSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) { // Padding disesuaikan agar rapi
        Text(
            text = "Info Sensus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
        )

        // 1. SENSUS PENDUDUK (Nuansa Biru)
        SensusBanner(
            imageRes = R.drawable.banner_sp2020, // Pastikan ini logo SP2020
            backgroundColor = Color(0xFFD6F3FF), // Biru Pastel Muda
            textColor = Color(0xFF006898),       // Biru Tua untuk Teks
            title = "SENSUS\nPENDUDUK 2020",
            url = "https://sensus.bps.go.id/main/index/sp2020"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 2. SENSUS PERTANIAN (Nuansa Hijau)
        SensusBanner(
            imageRes = R.drawable.banner_st2023, // Pastikan ini logo ST2023
            backgroundColor = Color(0xFFD8F5CD), // Hijau Pastel Muda
            textColor = Color(0xFF2E7D32),       // Hijau Tua untuk Teks
            title = "SENSUS\nPERTANIAN 2023",
            url = "https://sensus.bps.go.id/main/index/st2023"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3. SENSUS EKONOMI (Nuansa Oranye/Kuning)
        SensusBanner(
            imageRes = R.drawable.banner_se2026_2, // Pastikan ini logo SE2026
            backgroundColor = Color(0xFFFFEAB6), // Oranye/Kuning Pastel
            textColor = Color(0xFFB56C00),       // Coklat/Oranye Tua untuk Teks
            title = "SENSUS\nEKONOMI 2026",
            url = "https://sensus.bps.go.id/main/index/se2026"
        )
    }
}

@Preview
@Composable
fun InfoSensusSectionPreview() {
    InfoSensusSection()
}