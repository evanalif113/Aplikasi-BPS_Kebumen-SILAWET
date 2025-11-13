package com.example.bps.ui.statistik

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController // <-- Tambah import ini untuk Preview
import com.example.bps.R
import com.example.bps.components.SearchBar

@Composable
fun StatistikScreen(navController: NavController) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Search bar (reuse component)
        SearchBar()
        Spacer(modifier = Modifier.height(24.dp))

        // Category Cards
        StatCategoryCard(
            backgroundColor = Color(0xFF03A9F4), // Orange
            iconRes = R.drawable.ic_demografi, // replace with correct icon resource
            title = "Statistik Demografi dan Sosial",
            showArrow = true,
            onClick = { navController.navigate("subject_list/1") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        StatCategoryCard(
            backgroundColor = Color(0xFFFF9800), // Green
            iconRes = R.drawable.ic_ekonomi, // replace with correct icon resource
            title = "Statistik Ekonomi",
            showArrow = true,
            onClick = { navController.navigate("subject_list/2") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        StatCategoryCard(
            backgroundColor = Color(0xFF4CAF50), // Blue
            iconRes = R.drawable.ic_lingkungan,
            title = "Statistik Lingkungan Hidup dan Multi Domain",
            showArrow = true,
            onClick = { navController.navigate("subject_list/3") }
        )
    }
}

@Composable
private fun StatCategoryCard(
    backgroundColor: Color,
    iconRes: Int,
    title: String,
    showArrow: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        modifier =
            Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp).clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = CardDefaults.shape
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            if (showArrow) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Lihat detail",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatistikScreenPreview() {
    // Perbaiki preview agar tidak error
    StatistikScreen(navController = rememberNavController())
}