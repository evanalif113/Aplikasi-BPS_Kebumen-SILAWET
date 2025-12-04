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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.*

// Data Class
data class MenuData(
    val iconRes: Int,
    val title: String,
    val colorCard: Color
)

@Composable
fun MenuItemSection() {
    // Daftar menu (Total 12 Item untuk 3x4)
    /*val menuList = listOf(
        MenuData(R.drawable.penduduk, "Penduduk", Blue400),
        MenuData(R.drawable.tenaga_kerja, "Tenaga Kerja", Orange400),
        MenuData(R.drawable.pengangguran, "Pengangguran", Red400),
        MenuData(R.drawable.kemiskinan, "Kemiskinan", Green400),
        MenuData(R.drawable.gini_rasio_dan_ketimpangan, "Rasio GINI", Purple400),
        MenuData(R.drawable.ipm_ipg_idg, "IPM", Teal400),
        MenuData(R.drawable.pendidikan, "Pendidikan", Yellow400),
        MenuData(R.drawable.perumahan, "Perumahan", Indigo400),
        MenuData(R.drawable.pertanian, "Pertanian", Lime400),
        MenuData(R.drawable.pertumbuhan_ekonomi, "Pertumbuhan Ekonomi", Rose400),
        MenuData(R.drawable.pdrb, "PDRB", Cyan400),
        MenuData(R.drawable.perumahan, "Perumahan", Gray400)
    )*/

    val menuList = listOf(
    MenuData(R.drawable.penduduk, "Penduduk", White),
    MenuData(R.drawable.tenaga_kerja, "Tenaga Kerja", White),
    MenuData(R.drawable.pengangguran, "Pengangguran", White),
    MenuData(R.drawable.kemiskinan, "Kemiskinan", White),
    MenuData(R.drawable.gini_rasio_dan_ketimpangan, "Rasio GINI", White),
    MenuData(R.drawable.ipm_ipg_idg, "IPM", White),
    MenuData(R.drawable.pendidikan, "Pendidikan", White),
    MenuData(R.drawable.perumahan, "Perumahan", White),
    MenuData(R.drawable.pertanian, "Pertanian", White),
    MenuData(R.drawable.pertumbuhan_ekonomi, "Pertumbuhan Ekonomi", White),
    MenuData(R.drawable.pdrb, "PDRB", White),
    MenuData(R.drawable.perumahan, "Perumahan", White)
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
                // Atur tinggi fix yang cukup untuk 4 baris agar tidak perlu scroll
                // (Sekitar 100dp per baris + padding)
                .height(460.dp)
                .padding(vertical = 16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            // Matikan scroll internal grid agar semua item langsung tampil
            userScrollEnabled = false
        ) {
            items(menuList) { menu ->
                MenuItem(
                    iconRes = menu.iconRes,
                    title = menu.title,
                    colorCard = menu.colorCard
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    iconRes: Int,
    title: String,
    colorCard: Color,
    iconSize: Dp = 32.dp,
    textSize: TextUnit = 12.sp
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { }
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

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewMenuItemSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        MenuItemSection()
    }
}