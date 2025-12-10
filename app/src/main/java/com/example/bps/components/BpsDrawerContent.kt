package com.example.bps.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.Black
import com.example.bps.theme.White
import com.example.bps.theme.Gray500

@Composable
fun BpsDrawerContent(
    onNavigate: (String) -> Unit,
    onOpenLink: (String) -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = White,
    ) {
        // --- HEADER DRAWER ---
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Menu Lengkap",
            modifier = Modifier.padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        HorizontalDivider()

        // --- KELOMPOK 1: ARSIP & DATA ---
        Text(
            text = "Arsip & Data",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )

        NavigationDrawerItem(
            label = { Text(text = "Publikasi") },
            icon = { Icon(painterResource(id = R.drawable.ic_book_marked_24dp), contentDescription = null) },
            selected = false,
            onClick = {
                onClose()
                onNavigate("all_publications")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Berita Resmi Statistik") },
            icon = { Icon(painterResource(id = R.drawable.ic_grafik_24dp), contentDescription = null) },
            selected = false,
            onClick = {
                onClose()
                onNavigate("all_brs")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Galeri Infografis") },
            icon = { Icon(painterResource(id = R.drawable.ic_pie_chart_24dp), contentDescription = null) },
            selected = false,
            onClick = {
                onClose()
                onNavigate("all_infografis")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Berita Kegiatan") },
            icon = { Icon(painterResource(id = R.drawable.ic_menu_24dp), contentDescription = null) },
            selected = false,
            onClick = {
                onClose()
                onNavigate("all_news")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // --- KELOMPOK 2: LAYANAN & PROFIL ---
        Text(
            text = "Layanan & Profil",
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Gray500
        )

        NavigationDrawerItem(
            label = { Text(text = "Tentang Aplikasi SILAWET") },
            icon = { Icon(painterResource(id = R.drawable.ic_feedback), contentDescription = null) },
            selected = false,
            onClick = {
                onClose()
                onNavigate("about_screen")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Website Resmi BPS") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_internet_filled),
                    contentDescription = null,
                    tint = Color(0xFF0D47A1)
                )
            },
            selected = false,
            onClick = {
                onClose()
                onOpenLink("https://kebumenkab.bps.go.id")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        // --- BAGIAN SOSIAL MEDIA ---

        NavigationDrawerItem(
            label = { Text(text = "Facebook BPS Kebumen") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_facebook_fill),
                    contentDescription = null,
                    tint = Color(0xFF1877F2)
                )
            },
            selected = false,
            onClick = {
                onClose()
                onOpenLink("https://www.facebook.com/p/Bps-Kebumen-61556651832018/")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Instagram BPS Kebumen") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_instagram_fill),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            },
            selected = false,
            onClick = {
                onClose()
                onOpenLink("https://www.instagram.com/bpskebumen/")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = "Hubungi Via WhatsApp") },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_whatsapp_fill),
                    contentDescription = null,
                    tint = Color(0xFF25D366)
                )
            },
            selected = false,
            onClick = {
                onClose()
                onOpenLink("https://wa.me/6285179763305")
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
fun BpsDrawerContentPreview() {
    BpsDrawerContent(
        onNavigate = {},
        onOpenLink = {},
        onClose = {}
    )
}