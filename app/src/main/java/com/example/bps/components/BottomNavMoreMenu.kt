package com.example.bps.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.R
import com.example.bps.theme.Gray800
import com.example.bps.theme.Sky500
import com.example.bps.theme.White

@Composable
fun BottomNavWithMoreMenu(
    navController: NavController,
    currentRoute: String?
) {
    val context = LocalContext.current

    // Daftar Menu Bottom Nav
    val items = listOf(
        Triple("Beranda", R.drawable.ic_house_24dp, "beranda"),
        Triple("Statistik", R.drawable.ic_grafik_24dp, "statistik"),
        Triple("Infografik", R.drawable.ic_open_book_24dp, "infografik"),
        // Ubah menu "Lainnya" menjadi "WhatsApp"
        Triple("Layanan", R.drawable.ic_whatsapp_fill, "whatsapp")
    )

    BottomAppBar(containerColor = Sky500) {
        items.forEach { (title, iconRes, route) ->
            val isSelected = currentRoute == route

            BottomNavItem(
                title = title,
                iconRes = iconRes,
                selected = isSelected,
                onClick = {
                    if (route == "whatsapp") {
                        // --- LOGIKA LANGSUNG BUKA WHATSAPP ---
                        try {
                            // GANTI NOMOR DI SINI (Format: 628...)
                            val phoneNumber = "6285179763305"
                            val message = "Halo BPS Kebumen, saya ingin bertanya..."

                            val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(url)
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        // Navigasi Biasa
                        navController.navigate(route) {
                            // Agar tidak menumpuk stack navigasi
                            popUpTo("beranda") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

// --- HELPER: ITEM NAVIGASI BAWAH ---
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
                },
                onTap = { onClick() }
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

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavWithMoreMenu() {
    val navController = rememberNavController()
    BottomNavWithMoreMenu(
        navController = navController,
        currentRoute = "beranda"
    )
}