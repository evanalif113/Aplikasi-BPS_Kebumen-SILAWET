package com.example.bps.ui.general

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bps.ui.infografik.news.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralDetailScreen(
    navController: NavController,
    viewModel: NewsViewModel,
    id: Int,
    contentType: ContentType // PUBLIKASI atau NEWS/BRS/INFOGRAFIS
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 1. Cari Data berdasarkan ID & Tipe
    val itemData = remember(id, contentType) {
        when (contentType) {
            ContentType.PUBLIKASI -> viewModel.getPublicationById(id)

            // Panggil fungsi KHUSUS BRS
            ContentType.BRS -> viewModel.getBrsById(id)

            // Panggil fungsi KHUSUS Infografis
            ContentType.INFOGRAFIS -> viewModel.getInfografikById(id)

            // Panggil fungsi KHUSUS Berita Kegiatan
            ContentType.NEWS -> viewModel.getNewsActivityById(id)
        }
    }

    // Jika data tidak ditemukan (misal refresh), kembali saja
    if (itemData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Data tidak ditemukan")
            Button(onClick = { navController.popBackStack() }) { Text("Kembali") }
        }
        return
    }

    // 2. Ekstrak Data agar UI-nya generik
    val title = if (contentType == ContentType.PUBLIKASI) (itemData as com.example.bps.data.remote.responses.PublicationItem).title else (itemData as com.example.bps.data.remote.responses.NewsItem).title

    val date = if (contentType == ContentType.PUBLIKASI) (itemData as com.example.bps.data.remote.responses.PublicationItem).getSimpleDate() else (itemData as com.example.bps.data.remote.responses.NewsItem).getSimpleDate()

    val imageUrl = if (contentType == ContentType.PUBLIKASI) (itemData as com.example.bps.data.remote.responses.PublicationItem).coverUrl else (itemData as com.example.bps.data.remote.responses.NewsItem).getDisplayImage()

    val descRaw = if (contentType == ContentType.PUBLIKASI) (itemData as com.example.bps.data.remote.responses.PublicationItem).abstract else (itemData as com.example.bps.data.remote.responses.NewsItem).getSummary()

    val linkUrl = if (contentType == ContentType.PUBLIKASI) (itemData as com.example.bps.data.remote.responses.PublicationItem).pdfUrl else (itemData as com.example.bps.data.remote.responses.NewsItem).link

    // Helper bersihkan HTML tag dari deskripsi
    val cleanDesc = HtmlCompat.fromHtml(descRaw ?: "Tidak ada deskripsi", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Konten", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Tombol Download / Baca Selengkapnya
            if (!linkUrl.isNullOrEmpty()) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if(contentType == ContentType.PUBLIKASI) Icons.Default.Download else Icons.Default.Link,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if(contentType == ContentType.PUBLIKASI) "Unduh PDF" else "Baca Selengkapnya di Web")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // GAMBAR UTAMA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    // Tinggi Box bisa disamakan atau disesuaikan
                    // Jika Fit, tinggi bisa lebih fleksibel, misal 280.dp atau 300.dp untuk semua.
                    .height(300.dp) // <-- Tinggi Box disamakan untuk semua
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit, // <--- Semua akan Fit
                    modifier = Modifier.fillMaxSize() // Pastikan gambar mengisi Box secara utuh
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // JUDUL
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TANGGAL
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = date, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // DESKRIPSI / ABSTRAK
            Text(
                text = "Deskripsi:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cleanDesc,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )
        }
    }
}