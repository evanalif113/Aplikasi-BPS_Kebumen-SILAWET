package com.example.bps.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bps.components.SearchBar // Import SearchBar ASLI

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = viewModel()
) {
    var queryText by remember { mutableStateOf("") }
    val uiState by viewModel.searchState.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus() // Auto keyboard muncul
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = 2.dp, color = Color.White) {
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    SearchBar(
                        query = queryText,
                        onQueryChange = { queryText = it },
                        onSearch = { viewModel.searchDatasets(queryText) },
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val state = uiState) {
                is SearchUiState.Idle -> Text("Ketik kata kunci lalu tekan enter...", Modifier.align(Alignment.Center), color = Color.Gray)
                is SearchUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is SearchUiState.Error -> Text(state.message, Modifier.align(Alignment.Center), color = Color.Red)
                is SearchUiState.Success -> {
                    if (state.data.isEmpty()) {
                        Text("Tidak ada data ditemukan.", Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.data) { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        navController.navigate("detail_screen/${item.id}")
                                    },
                                    elevation = CardDefaults.cardElevation(2.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(item.subject, style = MaterialTheme.typography.labelSmall, color = Color(0xFFE65100))
                                        Spacer(Modifier.height(4.dp))
                                        Text(item.dataset_name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(4.dp))
                                        Text("Update: ${item.updated_at ?: "-"}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}