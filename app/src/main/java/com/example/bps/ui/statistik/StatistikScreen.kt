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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bps.R
import com.example.bps.components.SearchBar
import com.example.bps.ui.statistik.datasetdetail.DatasetDetailScreen
import com.example.bps.ui.statistik.SubjectList.SubjectListScreen

@Composable
fun StatistikScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Search bar (Komponen Reuse)
        SearchBar()

        Spacer(modifier = Modifier.height(24.dp))

        // --- KATEGORI STATISTIK ---

        // 1. Statistik Demografi dan Sosial
        StatCategoryCard(
            backgroundColor = Color(0xFF03A9F4), // Light Blue
            iconRes = R.drawable.ic_demografi, // Ganti dengan icon yang sesuai (misal ic_demografi)
            title = "Statistik Demografi dan Sosial",
            showArrow = true,
            onClick = { navController.navigate("subject_list/1") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 2. Statistik Ekonomi
        StatCategoryCard(
            backgroundColor = Color(0xFFFF9800), // Orange
            iconRes = R.drawable.ic_ekonomi, // Ganti dengan icon yang sesuai (misal ic_ekonomi)
            title = "Statistik Ekonomi",
            showArrow = true,
            onClick = { navController.navigate("subject_list/2") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 3. Statistik Lingkungan Hidup
        StatCategoryCard(
            backgroundColor = Color(0xFF4CAF50), // Green
            iconRes = R.drawable.ic_lingkungan, // Ganti dengan icon yang sesuai (misal ic_lingkungan)
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
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = CardDefaults.shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
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

// --- NAVIGATION GRAPH EXTENSION ---
// Fungsi ini menampung rute-rute turunan dari Statistik agar MainActivity lebih bersih

fun NavGraphBuilder.statistikGraph(navController: NavController) {

    // 1. Subject List (Daftar Subjek berdasarkan Kategori)
    composable(
        route = "subject_list/{categoryId}",
        arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
    ) { backStackEntry ->
        val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "0"
        SubjectListScreen(categoryId = categoryId, navController = navController)
    }

    // 2. Dataset List (Daftar Tabel berdasarkan Subjek)
    composable(
        route = "dataset_list/{subjectName}",
        arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
    ) { backStackEntry ->
        val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
        DatasetListScreen(subjectName = subjectName, navController = navController)
    }

    // 3. Dataset Detail (Detail Tabel)
    composable(
        route = "detail_screen/{datasetId}",
        arguments = listOf(navArgument("datasetId") { type = NavType.StringType })
    ) { backStackEntry ->
        val datasetId = backStackEntry.arguments?.getString("datasetId") ?: ""
        DatasetDetailScreen(datasetId = datasetId, navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun StatistikScreenPreview() {
    StatistikScreen(navController = rememberNavController())
}