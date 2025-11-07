package com.example.bps.ui.statistik

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListScreen(
    categoryId: String, // Menerima ID Kategori (misal "2")
    navController: NavController,
    viewModel: SubjectListViewModel = viewModel() // ViewModel baru
) {
    val uiState by viewModel.uiState

    // Cari daftar subject yang sesuai dengan categoryId
    val categoryInt = categoryId.toIntOrNull()
    val subjects = uiState.categoriesMap
        .find { it.category == categoryInt } // Cari kategori (misal: 2)
        ?.subjects ?: emptyList() // Ambil daftar subjects-nya (misal: ["Neraca Ekonomi"])

    // Beri judul
    val title = when (categoryInt) {
        1 -> "Statistik Demografi dan Sosial"
        2 -> "Statistik Ekonomi"
        3 -> "Statistik Lingkungan Hidup"
        else -> "Daftar Subjek"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                }
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
                                        // Navigasi ke Layar 3 (Daftar Tabel)
                                        // Kirim NAMA subject-nya
                                        navController.navigate("dataset_list/${subjectName}")
                                    }
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