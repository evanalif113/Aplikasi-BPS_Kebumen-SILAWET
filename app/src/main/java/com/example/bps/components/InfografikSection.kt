package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.bps.theme.Gray200

data class InfografikVariable(
    val date: String,
    val title: String,
    val imageUrl: String
)

@Composable
fun InfografikCard(infografikVal: InfografikVariable) {
    Card(
        modifier = Modifier.width(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            // Gambar dari URL menggunakan Coil AsyncImage
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Gray200)
            ) {
                AsyncImage(
                    model = infografikVal.imageUrl,
                    contentDescription = infografikVal.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Konten teks di bawah gambar
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = infografikVal.date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = infografikVal.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun InfografikSection() {
    // Data dummy untuk ditampilkan
    val newsList = listOf(
        InfografikVariable(
            "8 Oktober 2025",
            "RESMI RILIS! Booklet SAKERNAS Agustus 2024 Telah Terbit!",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRj76KyQ_kxext54OeEe3Z0VsfpUmUmtIZ9mw&s"
        ),
        InfografikVariable(
            "7 Oktober 2025",
            "Alamat Misterius, Data Sensus Serius",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRj76KyQ_kxext54OeEe3Z0VsfpUmUmtIZ9mw&s"
        ),
        InfografikVariable(
            "2 Oktober 2025",
            "Selamat Hari Batik Nasional 2025",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRj76KyQ_kxext54OeEe3Z0VsfpUmUmtIZ9mw&s"
        ),
        InfografikVariable(
            "2 Oktober 2025",
            "Pengolahan Peta Wilkerstat",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRj76KyQ_kxext54OeEe3Z0VsfpUmUmtIZ9mw&s"
        )
    )

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        // Beri padding horizontal sama dengan contentPadding LazyRow sehingga sejajar
        Text(
            text = "Infografis",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(newsList) { newsItem ->
                InfografikCard(infografikVal = newsItem)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfografikSectionPreview() {
    InfografikSection()
}