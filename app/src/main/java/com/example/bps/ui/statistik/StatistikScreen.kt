package com.example.bps.ui.statistik

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
// GANTI IMPORT SEARCH BAR
import com.example.bps.components.HomeSearchBar
import com.example.bps.ui.statistik.datasetdetail.DatasetDetailScreen
import com.example.bps.ui.statistik.subjectlist.SubjectListScreen
// Pastikan import DatasetListScreen ada
import com.example.bps.ui.statistik.DatasetListScreen

@Composable
fun StatistikScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // --- PERBAIKAN DI SINI ---
        // Gunakan HomeSearchBar agar konsisten dengan Beranda
        HomeSearchBar(
            onSearchClicked = {
                navController.navigate("search_screen")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- KATEGORI STATISTIK ---

        // 1. Statistik Demografi dan Sosial
        StatCategoryCard(
            backgroundColor = Color(0xFF03A9F4), // Light Blue
            iconRes = R.drawable.ic_demografi,
            title = "Statistik Demografi dan Sosial",
            onClick = { navController.navigate("subject_list/1") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 2. Statistik Ekonomi
        StatCategoryCard(
            backgroundColor = Color(0xFFFF9800), // Orange
            iconRes = R.drawable.ic_ekonomi,
            title = "Statistik Ekonomi",
            onClick = { navController.navigate("subject_list/2") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 3. Statistik Lingkungan Hidup
        StatCategoryCard(
            backgroundColor = Color(0xFF4CAF50), // Green
            iconRes = R.drawable.ic_lingkungan,
            title = "Statistik Lingkungan Hidup dan Multi Domain",
            onClick = { navController.navigate("subject_list/3") }
        )
    }
}

@Composable
private fun StatCategoryCard(
    backgroundColor: Color,
    iconRes: Int,
    title: String,
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
        }
    }
}

// --- NAVIGATION GRAPH EXTENSION ---
// Fungsi ini berguna untuk merapikan NavHost di MainActivity.
// Jika di MainActivity sudah ada composable() untuk rute-rute ini,
// fungsi ini opsional (boleh dipanggil, boleh tidak, asal jangan duplikat).

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