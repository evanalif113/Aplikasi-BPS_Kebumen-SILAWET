package com.example.bps.ui.statistik.datasetdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.components.ChartSection
import com.example.bps.components.TabelDataSection
import com.example.bps.data.remote.responses.Insight

// Warna Biru BPS untuk Tab Aktif
val BpsBlue = Color(0xFF0D47A1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    datasetId: String,
    navController: NavController,
    viewModel: DetailDatasetViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    // State untuk Tab yang dipilih (0 = Grafik, 1 = Tabel)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Chart", "Tabel Data")

    // Load data awal
    LaunchedEffect(datasetId) {
        if (uiState.dataset == null) {
            viewModel.getDatasetDetail(datasetId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Dataset", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${uiState.error}", color = Color.Red)
                        Button(onClick = { viewModel.getDatasetDetail(datasetId) }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                uiState.dataset != null -> {
                    val data = uiState.dataset!!

                    // Scrollable Parent Column
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        // --- BAGIAN 1: HEADER & FILTER ---
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Judul
                            Text(
                                text = data.dataset.dataset_name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                            Text(
                                text = "Sumber: ${data.dataset.source}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            // Filter Tahun & Mode
                            if (data.available_years.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // 1. DROPDOWN TAHUN (Kiri)
                                    if (data.available_years.isNotEmpty()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Tahun: ", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                            YearDropdown(
                                                years = data.available_years,
                                                selectedYear = data.current_year ?: data.available_years.first(),
                                                onYearSelected = { newYear ->
                                                    viewModel.getDatasetDetail(datasetId, newYear)
                                                }
                                            )
                                        }
                                    }

                                    // 2. DROPDOWN MODE TAMPILAN (Kanan)
                                    val isPopulationData = data.dataset.dataset_name.contains("Penduduk", ignoreCase = true) &&
                                            data.dataset.dataset_name.contains("Kecamatan", ignoreCase = true)

                                    if (isPopulationData) {
                                        ModeDropdown(
                                            onModeSelected = { selectedMode ->
                                                viewModel.getDatasetDetail(datasetId, data.current_year, selectedMode)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // --- BAGIAN 2: TAB SWITCHER ---
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = Color.White,
                            contentColor = BpsBlue,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                                    color = BpsBlue
                                )
                            }
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Text(
                                            title,
                                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTabIndex == index) BpsBlue else Color.Gray
                                        )
                                    }
                                )
                            }
                        }

                        // --- BAGIAN 3: KONTEN SESUAI TAB ---
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            when (selectedTabIndex) {
                                0 -> {
                                    // === ISI TAB 1: GRAFIK & INSIGHT ===
                                    Column {
                                        // Grafik
                                        if (data.chart != null) {
                                            Text("Visualisasi Data", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                                            ChartSection(chartData = data.chart)
                                            Spacer(modifier = Modifier.height(24.dp))
                                        } else {
                                            Text("Grafik tidak tersedia untuk data ini.", color = Color.Gray)
                                        }

                                        // Insight / Analisis
                                        if (data.insights.isNotEmpty()) {
                                            Text("Analisis Data (Insight)",
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                            data.insights.forEach { insight ->
                                                InsightCard(insight)
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    // === ISI TAB 2: DATA TABEL ===
                                    Column {
                                        Text("Rincian Data Tabel", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                                        TabelDataSection(tableData = data.table)

                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            "Geser tabel ke samping jika kolom terpotong.",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            fontStyle = FontStyle.Italic
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
}

// --- KOMPONEN PENDUKUNG ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdown(
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var displayText by remember(selectedYear) { mutableStateOf(selectedYear.toString()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(110.dp)
    ) {
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // PERBAIKAN: Gunakan Modifier.menuAnchor() dengan MenuAnchorType.PrimaryEditable
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BpsBlue,
                unfocusedBorderColor = Color.LightGray
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = {
                        displayText = year.toString()
                        expanded = false
                        onYearSelected(year)
                    }
                )
            }
        }
    }
}

@Composable
fun InsightCard(insight: Insight) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFBBDEFB))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(insight.title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(insight.value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BpsBlue)
            Spacer(modifier = Modifier.height(4.dp))
            Text(insight.description, fontSize = 14.sp, color = Color(0xFF424242))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeDropdown(onModeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var displayText by remember { mutableStateOf("Gender") }

    val options = listOf(
        "Gender" to "gender",
        "Kecamatan" to "region"
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(140.dp)
    ) {
        // --- BUNGKUS DALAM BOX AGAR RAPI ---
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                // PERBAIKAN: Gunakan Modifier.menuAnchor()
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BpsBlue,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                label = { Text("Tampilan", fontSize = 11.sp) }
            )

            // Canvas transparan di atas TextField (agar bisa diklik di semua area)
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
            ) {}
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { (label, value) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        displayText = label
                        expanded = false
                        onModeSelected(value)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DatasetDetailScreenPreview() {
    val navController = rememberNavController()
    DatasetDetailScreen(
        datasetId = "1",
        navController = navController
    )
}