package com.example.bps.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.*

// 1. Update Data Class: Tambahkan 'apiSubject'
data class MenuData(
    val iconRes: Int,
    val title: String,
    val colorCard: Color,
    val slug: String // <-- Ganti apiSubject jadi slug
)

@Composable
fun MenuItemSection(
    onItemClick: (String) -> Unit
) {
    // ISI DATA DENGAN SLUG YANG BENAR (Sesuai JSON API)
    val menuList = listOf(
        MenuData(R.drawable.penduduk, "Penduduk", White, "kependudukan"),
        MenuData(R.drawable.tenaga_kerja, "Tenaga Kerja", White, "tenaga-kerja"),
        MenuData(R.drawable.pengangguran, "Pengangguran", White, "pengangguran"),
        MenuData(R.drawable.kemiskinan, "Kemiskinan", White, "kemiskinan"),
        MenuData(R.drawable.gini_rasio_dan_ketimpangan, "Rasio GINI", White, "rasio-gini"),
        MenuData(R.drawable.ipm_ipg_idg, "IPM", White, "ipm"),
        MenuData(R.drawable.inflasi, "Inflasi", White, "inflasi"),
        MenuData(R.drawable.pertumbuhan_ekonomi, "Ekonomi", White, "ekonomi"),
        MenuData(R.drawable.pdrb, "PDRB", White, "pdrb"),
        MenuData(R.drawable.pendidikan, "Pendidikan", White, "pendidikan"),
        MenuData(R.drawable.perumahan, "Perumahan", White, "perumahan"),
        MenuData(R.drawable.pertanian, "Pertanian", White, "pertanian")
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Orange300),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp)
                .padding(vertical = 16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(menuList) { menu ->
                MenuItem(
                    iconRes = menu.iconRes,
                    title = menu.title,
                    colorCard = menu.colorCard,
                    // 3. Saat diklik, kirim rute dinamis: "dataset_list/NamaSubjectAsli"
                    onClick = { onItemClick(menu.slug) }
                )
            }
        }
    }
}

// ... (Fungsi MenuItem di bawahnya biarkan saja, tidak berubah) ...
@Composable
fun MenuItem(
    iconRes: Int,
    title: String,
    colorCard: Color,
    onClick: () -> Unit,
    iconSize: Dp = 48.dp,
    textSize: TextUnit = 14.sp
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(colorCard),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(iconSize),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Black,
                fontSize = textSize,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 1
        )
    }
}