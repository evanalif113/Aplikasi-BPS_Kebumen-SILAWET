package com.example.bps.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.R

// Warna custom
val Sky500 = Color(0xFF03A9F4)
val AppWhite = Color(0xFFFFFFFF)
val Gray800 = Color(0xFF424242)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavWithMoreMenu(
    navController: NavController,
    currentRoute: String?
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val items = listOf(
        "Beranda" to R.drawable.ic_house_24dp,
        "Statistik" to R.drawable.ic_grafik_24dp,
        "Maps" to R.drawable.ic_geotag_24dp,
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
                selected = isSelected, // <-- Gunakan logika baru
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
            containerColor = AppWhite,
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Menu Lainnya", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(16.dp))
                SheetTextItem("Profil") { showSheet = false }
                SheetTextItem("Pengaturan") { showSheet = false }
                SheetTextItem("Tentang Aplikasi") { showSheet = false }
                SheetTextItem("Bantuan") { showSheet = false }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { showSheet = false }) { Text("Tutup") }
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem( // <-- INI PERUBAHAN UTAMANYA
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
                    selected -> AppWhite
                    isHovered -> AppWhite.copy(alpha = 0.7f)
                    else -> Gray800
                }
            )
        },
        label = {
            Text(
                title,
                color = when {
                    selected -> AppWhite
                    isHovered -> AppWhite.copy(alpha = 0.7f)
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
            selectedIconColor = AppWhite,
            selectedTextColor = AppWhite,
            unselectedIconColor = Gray800,
            unselectedTextColor = Gray800,
            indicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SheetTextItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        color = Color.Black
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