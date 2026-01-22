package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.theme.*

/**
 * TopBar custom untuk halaman detail/child Mirip dengan TopAppBar di MainActivity tapi dengan arrow
 * back di kiri dan tanpa hamburger menu
 */
@Composable
fun BpsChildTopBar(title: String, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
        // Box dengan tinggi tetap dan background orange (sama seperti MainScreen)
        Box(
                modifier =
                        modifier.fillMaxWidth()
                                .height(64.dp) // Tinggi TopAppBar standar Material3
                                .background(Orange400),
                contentAlignment = Alignment.CenterStart
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        // Tombol Back Arrow (menggantikan hamburger menu)
                        IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali",
                                        tint = Gray800,
                                        modifier = Modifier.size(24.dp)
                                )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Title Text (sama styling dengan MainScreen)
                        Text(
                                text = title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                        )

                        // Spacer kanan untuk balance (opsional)
                        Spacer(modifier = Modifier.width(12.dp))
                }
        }
}
