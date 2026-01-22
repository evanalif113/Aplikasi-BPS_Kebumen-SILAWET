package com.example.bps.ui.statistik.subjectlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Jangan lupa import Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.components.BpsChildTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListScreen(
    categoryId: String, // Menerima ID Kategori (misal "2")
    navController: NavController,
    viewModel: SubjectListViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val categoryInt = categoryId.toIntOrNull()
    val subjects = uiState.categories
        .find { it.category == categoryInt }
        ?.subjects ?: emptyList()

    // 1. Tentukan Judul
    val title = when (categoryInt) {
        1 -> "Statistik Demografi dan Sosial"
        2 -> "Statistik Ekonomi"
        3 -> "Statistik Lingkungan Hidup"
        else -> "Daftar Subjek"
    }

    // 2. Tentukan Warna TopBar Berdasarkan Kategori
    /*val topBarColor = when (categoryInt) {
        1 -> Color(0xFF03A9F4) // Biru (Sosial)
        2 -> Color(0xFFFF9800) // Kuning/Gold (Ekonomi)
        3 -> Color(0xFF4CAF50) // Hijau (Lingkungan/Pertanian)
        else -> Color(0xFFFF9800) // Default Orange (Sama kayak Beranda)
    }*/

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(title) },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
//                    }
//                },
//                // 3. Terapkan Warna di sini
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFFFF9800),
//                    titleContentColor = Color.White,       // Judul Putih
//                    navigationIconContentColor = Color.White // Ikon Back Putih
//                )
//            )
//        }
        topBar = {
            BpsChildTopBar(
                title = title,
                onBackClick = { navController.popBackStack() }
            )
        }

    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text("Error: ${uiState.error}", Modifier.align(Alignment.Center))
                }
                subjects.isEmpty() && !uiState.isLoading -> {
                    Text("Tidak ada subjek untuk kategori ini.", Modifier.align(Alignment.Center))
                }
                else -> {
                    // Tampilkan daftar Subject
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(subjects, key = { it }) { subjectName ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("dataset_list/${subjectName}")
                                    },
                                // Opsional: Beri sedikit warna pada card agar tidak terlalu polos
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = subjectName,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SubjectListScreenPreview() {
    val navController = rememberNavController()
    SubjectListScreen(
        categoryId = "2",
        navController = navController
    )
}