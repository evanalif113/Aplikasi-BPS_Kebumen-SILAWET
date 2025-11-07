package com.example.bps.ui.statistik

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
/**
 * Screen ini menampilkan DAFTAR dataset berdasarkan kategori/subject.
 *
 * @param categoryId Ini adalah 'subject' yang dikirim dari layar sebelumnya, misal "Ekonomi".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetListScreen(
    subjectName: String, // <-- Ganti nama parameter ini
    navController: NavController,
    viewModel: DatasetListViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    LaunchedEffect(subjectName) { // <-- Ganti ini
        viewModel.getDatasetList(subject = subjectName) // <-- Ganti ini
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subjectName) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                        Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali"
                                )
                            }
                        }
                )
            }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                // ---- KONDISI LOADING ----
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // ---- KONDISI ERROR ----
                uiState.error != null -> {
                    Column(
                            modifier = Modifier.align(Alignment.Center).padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                                text = "Error: ${uiState.error}",
                                color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.getDatasetList(subject = subjectName) }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                // ---- KONDISI SUKSES TAPI KOSONG ----
                !uiState.isLoading && uiState.datasets.isEmpty() -> {
                    Text(
                            text = "Tidak ada data untuk kategori ini.",
                            modifier = Modifier.align(Alignment.Center)
                    )
                }

                // ---- KONDISI SUKSES DAN ADA DATA ----
                else -> {
                    // Tampilkan daftar menggunakan LazyColumn
                    LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.datasets, key = { it.id }) { dataset ->
                            // Setiap item adalah Card yang bisa diklik
                            Card(
                                    modifier =
                                            Modifier.fillMaxWidth().clickable {
                                                // INI PENTING: Navigasi ke DetailScreen
                                                navController.navigate(
                                                        "detail_screen/${dataset.id}"
                                                )
                                            },
                                    colors =
                                            CardDefaults.cardColors(
                                                    containerColor =
                                                            MaterialTheme.colorScheme.surfaceVariant
                                            )
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                            text = dataset.dataset_name,
                                            style = MaterialTheme.typography.titleMedium,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                            text = "Subjek: ${dataset.subject}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
