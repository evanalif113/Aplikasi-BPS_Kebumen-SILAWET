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
import com.example.bps.data.remote.responses.GridDatasetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuGridScreen(
    slug: String,
    navController: NavController,
    viewModel: MenuDatasetViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    LaunchedEffect(slug) {
        viewModel.getDatasetsBySlug(slug)
    }

    // Ubah slug jadi judul rapi (opsional)
    val title = slug.replace("-", " ").uppercase()

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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.error != null -> Text("Error: ${uiState.error}", Modifier.align(Alignment.Center))
                !uiState.isLoading && uiState.datasets.isEmpty() -> Text("Data Kosong. Mohon Hubungi Kontak", Modifier.align(Alignment.Center))
                else -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.datasets) { dataset ->
                            GridItemCard(
                                item = dataset, // dataset ini adalah GridDatasetItem (punya id: Int)
                                onClick = {
                                    // Misal id-nya 6, maka route jadi: "detail_screen/6"
                                    navController.navigate("detail_screen/${dataset.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridItemCard(
    item: GridDatasetItem,
    onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(item.datasetName, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text("Subjek: ${item.subject}", style = MaterialTheme.typography.bodySmall)
        }
    }
}