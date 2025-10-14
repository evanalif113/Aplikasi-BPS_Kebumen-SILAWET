package com.example.bps.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bps.R
import com.example.bps.theme.Black
import com.example.bps.theme.Blue400
import com.example.bps.theme.Green300
import com.example.bps.theme.Orange300
import com.example.bps.theme.Red300
import com.example.bps.theme.White

@Composable
fun MenuItem(iconRes: Int, title: String, colorCard: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .size(72.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = Black,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            lineHeight = 14.sp
        )
    }
}

@Preview
@Composable
fun MenuItemPreview() {
    MenuItem(iconRes = R.drawable.ic_maps_24dp, title = "Peta", colorCard = Blue400)
}
@Composable
fun MenuItemSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Padding diubah menjadi 16.dp agar konsisten
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItem(iconRes = R.drawable.ic_maps_24dp, title = "Peta", colorCard = Blue400)
            MenuItem(iconRes = R.drawable.ic_grafik_24dp, title = "Statistik", colorCard = Orange300)
            MenuItem(iconRes = R.drawable.ic_open_book_24dp, title = "Infografik", colorCard = Red300)
            MenuItem(iconRes = R.drawable.ic_menu_24dp, title = "Lainnya", colorCard = Green300)
        }
    }
}

@Preview
@Composable
fun MenuItemSectionPreview() {
    MenuItemSection()
}

// 4. Composable untuk setiap item di dalam Menu Utama

