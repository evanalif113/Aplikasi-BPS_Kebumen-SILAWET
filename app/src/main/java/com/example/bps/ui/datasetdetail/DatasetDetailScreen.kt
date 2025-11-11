package com.example.bps.ui.datasetdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bps.components.TabelDataSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
        datasetId: String,
        navController: NavController,
        // Pastikan nama ViewModel Anda 'DetailDatasetViewModel'
        viewModel: DetailDatasetViewModel = viewModel()
) {
    // Ambil uiState dari ViewModel
    val uiState by viewModel.uiState

    // Pemicu untuk ambil data
    LaunchedEffect(datasetId) { viewModel.getDatasetDetail(datasetId) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Detail Dataset") },
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
                // --- KONDISI LOADING ---
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // --- KONDISI ERROR ---
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
                        Button(onClick = { viewModel.getDatasetDetail(datasetId) }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                // --- KONDISI SUKSES ---
                uiState.dataset != null -> {
                    val responseData =
                            uiState.dataset!! // Non-null assertion karena sudah dicek di branch

                    // Gunakan LazyColumn agar seluruh layar bisa scroll
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        // 1. TAMPILKAN JUDUL
                        item {
                            Text(
                                    text = responseData.dataset.dataset_name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // 2. TAMPILKAN INFO SUMBER
                        item {
                            Text(
                                    text = "Sumber: ${responseData.dataset.source}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // 3. TAMPILKAN JUDUL TABEL
                        item {
                            Text(
                                    text = "Data Tabel",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // 4. PANGGIL KOMPONEN TABEL
                        item { TabelDataSection(tableData = responseData.table) }

                        // 5. TAMPILKAN INSIGHTS
                        if (responseData.insights.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                        text = "Insights:",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                )
                                responseData.insights.forEach { insight ->
                                    Text(
                                            text = "• ${insight.title}: ${insight.value}",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }

                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}
