package com.example.bps.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.bps.R

/**
 * Composable untuk Kartu Layanan CANTIK (Konsultasi via WA)
 */
@Composable
fun CantikCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Kartu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_contact),
                    contentDescription = "CANTIK",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "CANTIK",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Chat Layanan Statistik Kebumen",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Tombol Aksi
            Button(
                onClick = {
                    openWhatsApp(context, "+6285179763305") // Nomor dari brosur
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    // --- INI PERBAIKANNYA ---
                    painter = painterResource(id = R.drawable.ic_whatsapp_fill),
                    contentDescription = "WhatsApp",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp), // Atur ukuran ikon
                    tint = Color.Unspecified // Hapus tint agar warna WA asli
                )
                Text("Konsultasi via WhatsApp")
            }
        }
    }
}

/**
 * Composable untuk Kartu Layanan LAPOR KAKANDA (Pengaduan)
 */
/**
 * Composable untuk Kartu Layanan LAPOR KAKANDA (Pengaduan)
 */
@Composable
fun LaporKakandaCard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Kartu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_feedback),
                    contentDescription = "LAPOR KAKANDA",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "LAPOR KAKANDA",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Laporan Saran, Masukan, & Pengaduan",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // Kumpulan Tombol Aksi
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tombol WA
                Button(
                    onClick = {
                        openWhatsApp(context, "+6285183110040") // Nomor dari brosur
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp_fill),
                        contentDescription = "WhatsApp",
                        // --- 1. TAMBAHKAN INI ---
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = Color.Unspecified
                    )
                    Text("Lapor via WhatsApp")
                }
                // Tombol Website
                Button(
                    onClick = {
                        // Link dari brosur
                        uriHandler.openUri("https://bps.go.id/bps3305_pengaduan")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_internet_filled),
                        contentDescription = "Website",
                        // --- 2. TAMBAHKAN INI ---
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = Color.Unspecified
                    )
                    Text("Lapor via Website")
                }
                // Tombol Email
                Button(
                    onClick = {
                        openEmail(context, "bps3305@bps.go.id") // Email dari brosur
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email_fill),
                        contentDescription = "Email",
                        // --- 3. TAMBAHKAN INI ---
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp),
                        tint = Color.Unspecified
                    )
                    Text("Lapor via Email")
                }
            }
        }
    }
}
// --- FUNGSI HELPER UNTUK MEMBUKA APLIKASI LAIN ---

fun openWhatsApp(context: Context, phoneNumber: String) {
    val number = phoneNumber.replace("+", "").replace(" ", "")
    val url = "https://api.whatsapp.com/send?phone=$number"

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp tidak terpasang.", Toast.LENGTH_SHORT).show()
    }
}

fun openEmail(context: Context, emailAddress: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Hanya aplikasi email
        putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        putExtra(Intent.EXTRA_SUBJECT, "Layanan BPS Kebumen")
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak ada aplikasi email.", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun LayananCardsPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CantikCard()
        LaporKakandaCard()
    }
}