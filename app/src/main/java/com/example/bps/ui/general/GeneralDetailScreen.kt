package com.example.bps.ui.general

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
import com.example.bps.data.remote.responses.NewsItemResponse
import com.example.bps.data.remote.responses.PublicationItemResponse
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.utils.launchInAppBrowser // Import fungsi browser internal

// 1. Data Class Penampung (Wrapper) agar kode UI bersih
private data class ContentDisplay(
    val title: String,
    val date: String,
    val imageUrl: String,
    val description: String?,
    val linkUrl: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralDetailScreen(
    navController: NavController,
    viewModel: NewsViewModel,
    id: Int,
    contentType: ContentType
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 2. Cari Data berdasarkan ID & Tipe
    val itemData = remember(id, contentType) {
        when (contentType) {
            ContentType.PUBLIKASI -> viewModel.getPublicationById(id)
            ContentType.BRS -> viewModel.getBrsById(id)
            ContentType.INFOGRAFIS -> viewModel.getInfografikById(id)
            ContentType.NEWS -> viewModel.getNewsById(id)
        }
    }

    // Jika data tidak ditemukan, tampilkan pesan error
    if (itemData == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Data tidak ditemukan")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navController.popBackStack() }) { Text("Kembali") }
            }
        }
        return
    }

    // 3. Konversi Data ke ContentDisplay (Hanya 1x logika if-else)
    val contentDisplay = remember(itemData) {
        if (contentType == ContentType.PUBLIKASI) {
            val item = itemData as PublicationItemResponse
            ContentDisplay(
                title = item.title,
                date = item.getSimpleDate(),
                imageUrl = item.coverUrl,
                description = item.abstract,
                linkUrl = item.pdfUrl
            )
        } else {
            // Berlaku untuk BRS, INFOGRAFIS, dan NEWS (Strukturnya sama: NewsItemResponse)
            val item = itemData as NewsItemResponse
            ContentDisplay(
                title = item.title,
                date = item.getSimpleDate(),
                imageUrl = item.getDisplayImage(),
                description = item.getSummary(),
                linkUrl = item.link
            )
        }
    }

    // Helper: Bersihkan HTML tag dari deskripsi
    val cleanDesc = remember(contentDisplay.description) {
        HtmlCompat.fromHtml(
            contentDisplay.description ?: "Tidak ada deskripsi",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        ).toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail Konten",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Tombol Download / Baca Selengkapnya dengan In-App Browser
            if (!contentDisplay.linkUrl.isNullOrEmpty()) {
                Button(
                    onClick = {
                        // MENGGUNAKAN IN-APP BROWSER
                        launchInAppBrowser(context, contentDisplay.linkUrl)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (contentType == ContentType.PUBLIKASI) Icons.Default.Download else Icons.Default.Link,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (contentType == ContentType.PUBLIKASI) "Unduh PDF" else "Baca Selengkapnya")
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
                    .height(250.dp) // Sedikit disesuaikan agar proporsional
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(contentDisplay.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // JUDUL
            Text(
                text = contentDisplay.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TANGGAL
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = contentDisplay.date, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(16.dp))

            // DESKRIPSI / ABSTRAK
            Text(
                text = "Deskripsi:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cleanDesc,
                fontSize = 14.sp,
                lineHeight = 24.sp, // Jarak antar baris supaya lebih enak dibaca
                color = Color.DarkGray
            )

            // Tambahan padding bawah agar teks tidak tertutup tombol floating (jika ada) atau bottom bar
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}