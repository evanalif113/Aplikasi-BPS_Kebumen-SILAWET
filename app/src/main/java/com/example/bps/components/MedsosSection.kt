package com.example.bps.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R

@Composable
fun MedsosFooter(modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ikuti Kami",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp) // Beri jarak lebih
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top // Rata atas agar teks sejajar
        ) {
            // --- 3. GANTI SEMUA ITEM DENGAN HELPER BARU ---
            item {
                MedsosItem(
                    painter = painterResource(id = R.drawable.ic_internet_filled),
                    label = "Website",
                    url = "https://kebumenkab.bps.go.id/",
                    uriHandler = uriHandler
                )
            }
            item {
                MedsosItem(
                    painter = painterResource(id = R.drawable.ic_instagram_fill),
                    label = "Instagram",
                    url = "https://www.instagram.com/bpskebumen/",
                    uriHandler = uriHandler
                )
            }
            item {
                MedsosItem(
                    painter = painterResource(id = R.drawable.ic_facebook_fill),
                    label = "Facebook",
                    url = "https://www.facebook.com/p/Bps-Kebumen-61556651832018/",
                    uriHandler = uriHandler
                )
            }
            item {
                MedsosItem(
                    painter = painterResource(id = R.drawable.ic_youtube_fill),
                    label = "YouTube",
                    url = "http://www.youtube.com/@bpskebumen3305",
                    uriHandler = uriHandler
                )
            }
        }
    }
}

/**
 * 4. BUAT COMPONENT HELPER BARU INI
 * Component ini berisi Icon + Teks di bawahnya, dan bisa diklik.
 */
@Composable
private fun MedsosItem(
    painter: Painter,
    label: String,
    url: String,
    uriHandler: UriHandler
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp), // Jarak antara ikon dan teks
        modifier = Modifier
            .clickable { uriHandler.openUri(url) }
            .padding(horizontal = 4.dp) // Beri sedikit padding
    ) {
        // IKON (Bagian atas)
        Icon(
            painter = painter,
            contentDescription = label, // Deskripsi di sini
            tint = Color.Unspecified, // Pakai warna asli
            modifier = Modifier.size(36.dp)
        )
        // TEKS (Bagian bawah)
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MedsosFooterPreview() {
    MedsosFooter()
}