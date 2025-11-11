package com.example.bps.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import com.example.bps.R


// Warna-warna custom
val Blue400 = Color(0xFF42A5F5)
val Orange400 = Color(0xFFFFA726)
val Red400 = Color(0xFFEF5350)
val Green400 = Color(0xFF66BB6A)
val White = Color(0xFFFFFFFF)

@Composable
fun MenuItemSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItem(iconRes = R.drawable.ic_maps_24dp, title = "Peta", colorCard = Blue400)
            MenuItem(iconRes = R.drawable.ic_grafik_24dp, title = "Statistik", colorCard = Orange400)
            MenuItem(iconRes = R.drawable.ic_open_book_24dp, title = "Infografik", colorCard = Red400)
            MenuItem(iconRes = R.drawable.ic_menu_24dp, title = "Lainnya", colorCard = Green400)
        }
    }
}

@Composable
fun MenuItem(
    iconRes: Int,
    title: String,
    colorCard: Color,
    iconSize: Dp = 36.dp, // Ukuran ikon lebih kecil
    textSize: TextUnit = 12.sp // Ukuran teks juga lebih kecil
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(colorCard.copy(alpha = 0.1f))
            .clickable { }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(iconSize + 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorCard),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(iconSize)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Black,
                fontSize = textSize,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

// 👇 Preview Section
@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PreviewMenuItemSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        MenuItemSection()
    }
}
