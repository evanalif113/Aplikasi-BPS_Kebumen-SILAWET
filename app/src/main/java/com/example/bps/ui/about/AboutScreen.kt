package com.example.bps.ui.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bps.R
import com.example.bps.components.MedsosFooter // Import komponen MedsosFooter
import com.example.bps.theme.Orange500 // Sesuaikan dengan warna tema Anda
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tentang Aplikasi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. LOGO & HEADER ---
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable { /* Easter egg bisa ditaruh sini */ }
            ) {
                // Ganti dengan Logo BPS atau Logo Aplikasi Anda
                Image(
                    painter = painterResource(id = R.drawable.logo), // GANTI DENGAN LOGO ANDA
                    contentDescription = "Logo SILAWET",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SILAWET",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Orange500
            )
            Text(
                text = "Sistem Layanan Wawasan E-Statistik",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Versi ${com.example.bps.BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 2. DESKRIPSI ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SILAWET adalah aplikasi resmi dari BPS Kabupaten Kebumen yang dirancang untuk memudahkan Anda dalam mengakses dan memahami data statistik terkini. Dapatkan wawasan mendalam melalui visualisasi data yang interaktif dan informatif.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- KERJA SAMA ---
            Text(
                text = "Kerja Sama",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aplikasi ini dikembangkan atas kerja sama antara BPS Kabupaten Kebumen dengan Program Studi S1 Sains Data Universitas Putra Bangsa.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                modifier = Modifier.align(Alignment.Start)
            )


            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. KONTAK & INFORMASI ---
            Text(
                text = "Hubungi Kami",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Alamat (Buka Maps)
            ContactRow(
                icon = Icons.Default.LocationOn,
                label = "Jl. Arungbinang No. 9, Kebumen",
                onClick = {
                    val gmmIntentUri = Uri.parse("geo:0,0?q=BPS+Kabupaten+Kebumen")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    try { context.startActivity(mapIntent) } catch (e: Exception) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=BPS+Kabupaten+Kebumen")))
                    }
                }
            )

            // Email (Buka Email App)
            ContactRow(
                icon = Icons.Default.Email,
                label = "bps3305@bps.go.id",
                onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:bps3305@bps.go.id")
                        putExtra(Intent.EXTRA_SUBJECT, "Tanya SILAWET")
                    }
                    try { context.startActivity(intent) } catch (e: Exception) {}
                }
            )

            // Telepon dan Website dihapus sesuai permintaan

            Spacer(modifier = Modifier.height(24.dp))

            // --- MEDSOS SECTION ---
            MedsosFooter()

            Spacer(modifier = Modifier.height(24.dp))

            // --- LOGO BERAKHLAK ---
            Image(
                painter = painterResource(id = R.drawable.berakhlak), // Pastikan file gambar bernama 'berakhlak' ada di folder drawable
                contentDescription = "Logo BerAKHLAK",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Sesuaikan tinggi sesuai kebutuhan
                    .padding(horizontal = 16.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 4. FOOTER ---
            Text(
                text = "© ${java.time.Year.now()} BPS Kabupaten Kebumen",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

// --- KOMPONEN BARIS KONTAK ---
@Composable
fun ContactRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Orange500,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
}

// --- PREVIEW UNTUK LAYAR FULL (ABOUT SCREEN) ---
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AboutScreenPreview() {
    // Di dalam Preview, kita harus membuat NavController palsu
    val navController = rememberNavController()

    MaterialTheme {
        AboutScreen(navController = navController)
    }
}