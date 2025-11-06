package com.example.bps.ui.datasetdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bps.data.remote.responses.BpsDatasetResponse

@Composable
fun DatasetDetailScreen(
        datasetId: String,
        navController: NavController,
        viewModel: DetailDatasetViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    // Panggil API saat datasetId berubah
    LaunchedEffect(datasetId) { viewModel.getDatasetDetail(datasetId) }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    ErrorSection(
                            error = uiState.error ?: "Terjadi kesalahan",
                            onRetry = { viewModel.getDatasetDetail(datasetId) }
                    )
                }
                uiState.dataset != null -> {
                    DatasetDetailContent(responseData = uiState.dataset!!)
                }
            }
        }
    }
}

@Composable
fun ErrorSection(error: String, onRetry: () -> Unit) {
    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun DatasetDetailContent(responseData: BpsDatasetResponse) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
                text = responseData.dataset.dataset_name,
                style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = "Sumber: ${responseData.dataset.source}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
                text = "Data Tabel",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
        )

        val table = responseData.table
        if (table != null) {
            TabelDataSection(tableData = table)
        } else {
            Text("Data tabel tidak tersedia", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun TabelDataSection(tableData: Any?) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        when (tableData) {
            null -> {
                Text("Data tabel tidak tersedia", style = MaterialTheme.typography.bodyMedium)
            }
            is List<*> -> {
                Text("Jumlah baris: ${tableData.size}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(tableData.size) { index ->
                        val row = tableData[index]
                        Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.surfaceVariant
                                        )
                        ) {
                            Text(
                                    text = row?.toString() ?: "-",
                                    modifier = Modifier.padding(12.dp),
                                    style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            else -> {
                Text(tableData.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
