package com.example.bps.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.R
import com.example.bps.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavWithMoreMenu(
    navController: NavController,
    currentRoute: String?
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uriHandler = LocalUriHandler.current // Untuk buka link WA/Web

    val items = listOf(
        "Beranda" to R.drawable.ic_house_24dp,
        "Statistik" to R.drawable.ic_grafik_24dp,
        "Peta" to R.drawable.ic_geotag_24dp,
        "Infografik" to R.drawable.ic_open_book_24dp,
        "Lainnya" to R.drawable.ic_menu_24dp
    )

    BottomAppBar(containerColor = Sky500) {
        items.forEach { (title, iconRes) ->
            val isSelected = if (title == "Lainnya") {
                showSheet
            } else {
                !showSheet && (currentRoute == title.lowercase())
            }

            BottomNavItem(
                title = title,
                iconRes = iconRes,
                selected = isSelected,
                onClick = {
                    if (title == "Lainnya") showSheet = true
                    else navController.navigate(title.lowercase())
                }
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Menu Lengkap",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // --- KELOMPOK 1: ARSIP & DATA ---
                SectionHeader("Arsip & Data")

                SheetMenuItem(
                    iconRes = R.drawable.ic_open_book_24dp, // GANTI dengan nama file icon buku Anda
                    title = "Publikasi dan Buku",
                    onClick = {
                        showSheet = false
                        navController.navigate("all_publications")
                    }
                )

                SheetMenuItem(
                    iconRes = R.drawable.ic_internet_filled, // GANTI dengan icon dokumen/kertas
                    title = "Berita Resmi Statistik",
                    onClick = {
                        showSheet = false
                        navController.navigate("all_brs")
                    }
                )

                SheetMenuItem(
                    iconRes = R.drawable.ic_feedback, // GANTI dengan icon gambar/galeri
                    title = "Galeri Infografis",
                    onClick = {
                        showSheet = false
                        navController.navigate("all_infografis")
                    }
                )

                SheetMenuItem(
                    iconRes = R.drawable.ic_lingkungan, // GANTI dengan icon koran/berita
                    title = "Berita Kegiatan",
                    onClick = {
                        showSheet = false
                        navController.navigate("all_news")
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                // --- KELOMPOK 2: LAYANAN ---
                SectionHeader("Layanan & Profil")

                SheetMenuItem(
                    iconRes = R.drawable.ic_feedback, // GANTI dengan icon info
                    title = "Tentang Aplikasi SILAWET",
                    onClick = { /* Navigasi About */ }
                )

                SheetMenuItem(
                    iconRes = R.drawable.ic_internet_filled, // GANTI dengan icon web/globe
                    title = "Website Resmi BPS Kebumen",
                    onClick = {
                        showSheet = false
                        uriHandler.openUri("https://kebumenkab.bps.go.id")
                    }
                )

                SheetMenuItem(
                    iconRes = R.drawable.ic_info_24dp, // GANTI dengan icon WA/Telepon
                    title = "Hubungi Kami Via WhatsApp",
                    onClick = {
                        showSheet = false
                        uriHandler.openUri("https://wa.me/6285179763305")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// --- HELPER: ITEM NAVIGASI BAWAH (TETAP SAMA) ---
@Composable
fun RowScope.BottomNavItem(
    title: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    NavigationBarItem(
        icon = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = when {
                    selected -> White
                    isHovered -> White.copy(alpha = 0.7f)
                    else -> Gray800
                }
            )
        },
        label = {
            Text(
                title,
                color = when {
                    selected -> White
                    isHovered -> White.copy(alpha = 0.7f)
                    else -> Gray800
                }
            )
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isHovered = true
                    tryAwaitRelease()
                    isHovered = false
                }
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = White,
            selectedTextColor = White,
            unselectedIconColor = Gray800,
            unselectedTextColor = Gray800,
            indicatorColor = Color.Transparent
        )
    )
}

// --- HELPER BARU: HEADER SECTION DI SHEET ---
@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

// --- HELPER BARU: ITEM MENU DI SHEET (LEBIH CANTIK) ---
@Composable
fun SheetMenuItem(
    iconRes: Int, // <-- Ubah tipe data jadi Int (ID Resource)
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ikon Menu (Kiri)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Sky500.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                // Ganti imageVector menjadi painterResource
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Sky500,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f))

        // Ikon Panah Kanan (Kanan) - Juga pakai XML
        Icon(
            painter = painterResource(id = R.drawable.ic_search_24dp), // Pastikan nama file xml panah Anda benar
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavWithMoreMenu() {
    val navController = rememberNavController()
    BottomNavWithMoreMenu(
        navController = navController,
        currentRoute = "beranda"
    )
}