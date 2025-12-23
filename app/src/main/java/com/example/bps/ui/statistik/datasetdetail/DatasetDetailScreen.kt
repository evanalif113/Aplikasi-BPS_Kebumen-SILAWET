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
import com.example.bps.components.ChartSection
import com.example.bps.components.TabelDataSection
import com.example.bps.data.remote.responses.Insight
import com.example.bps.theme.Orange300

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

    // State untuk Tab yang dipilih
    // 0 = Tabel Data, 1 = Chart
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf("Tabel Data", "Chart")

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Orange300)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${uiState.error}", color = Color.Red)
                        Button(onClick = { viewModel.getDatasetDetail(datasetId) }) { Text("Coba Lagi") }
                    }
                }
                uiState.dataset != null -> {
                    val data = uiState.dataset!!

                    // --- LOGIKA DEFAULT TAHUN TERBARU ---
                    // 1. Urutkan tahun dari Besar ke Kecil (Descending) agar user melihat tahun terbaru di atas
                    val sortedYears = remember(data.available_years) {
                        data.available_years.sortedDescending()
                    }

                    // 2. Tentukan tahun aktif.
                    // Jika data.current_year null (belum dipilih), gunakan tahun terbesar (maxOrNull).
                    val activeYear = data.current_year ?: data.available_years.maxOrNull() ?: 0

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // --- HEADER & FILTER ---
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                data.dataset.dataset_name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Sumber: ${data.dataset.source}", fontSize = 12.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(16.dp))

                            if (data.available_years.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // 1. DROPDOWN TAHUN
                                    if (data.available_years.isNotEmpty()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("Tahun: ", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                            YearDropdown(
                                                years = sortedYears, // Gunakan list yang sudah diurutkan
                                                selectedYear = activeYear, // Gunakan logic tahun terbaru
                                                onYearSelected = { newYear ->
                                                    viewModel.getDatasetDetail(datasetId, newYear)
                                                }
                                            )
                                        }
                                    }

                                    // 2. DROPDOWN MODE
                                    val isPopulationData = data.dataset.dataset_name.contains(
                                        "Penduduk", ignoreCase = true) &&
                                            data.dataset.dataset_name.contains(
                                                "Kecamatan", ignoreCase = true
                                            )
                                    if (isPopulationData) {
                                        ModeDropdown(onModeSelected = { selectedMode ->
                                            viewModel.getDatasetDetail(datasetId, activeYear, selectedMode)
                                        })
                                    }
                                }
                            }
                        }

                        // --- TABS ---
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
                                    text = { Text(title, color = if (selectedTabIndex == index) BpsBlue else Color.Gray) }
                                )
                            }
                        }

                        // --- CONTENT ---
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            when (selectedTabIndex) {
                                0 -> { // TABEL DATA
                                    Column {
                                        Text("Rincian Data Tabel", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))

                                        // --- FILTER TABEL SESUAI TAHUN AKTIF ---
                                        val currentYearStr = activeYear.toString()

                                        val filteredHeaders = data.table.headers.filterIndexed { index, header ->
                                            // Ambil kolom pertama (Wilayah) ATAU kolom yang sesuai tahun aktif (hilangkan .0)
                                            index == 0 || header.substringBefore(".") == currentYearStr
                                        }

                                        val filteredTableData = data.table.copy(headers = filteredHeaders)

                                        // Render Tabel
                                        if (filteredHeaders.size >= 2) {
                                            TabelDataSection(tableData = filteredTableData)
                                        } else {
                                            TabelDataSection(tableData = data.table)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text("Menampilkan data tahun $activeYear.", fontSize = 12.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
                                    }
                                }
                                1 -> { // CHART
                                    Column {
                                        if (data.chart != null) {
                                            Text("Visualisasi Data", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                                            ChartSection(chartData = data.chart)
                                            Spacer(modifier = Modifier.height(24.dp))
                                        } else {
                                            Text("Grafik tidak tersedia.", color = Color.Gray)
                                        }
                                        if (data.insights.isNotEmpty()) {
                                            Text("Analisis Data (Insight)", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                                            data.insights.forEach { InsightCard(it) }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearDropdown(
    years: List<Int>,
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // --- PERBAIKAN: Format Text Agar Tidak Ada .0 ---
    // Pastikan tahun dikonversi ke Int lalu ke String.
    // Jika inputnya 2024.0 (Double), toInt() akan membuang .0 nya.
    var displayText by remember(selectedYear) {
        mutableStateOf(selectedYear.toInt().toString())
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(110.dp)
    ) {
        OutlinedTextField(
            value = displayText, // Gunakan teks yang sudah diformat
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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
                // Format juga tampilan di dalam menu dropdown
                val yearLabel = year.toInt().toString()

                DropdownMenuItem(
                    text = { Text(yearLabel) }, // Tampilkan "2024", bukan "2024.0"
                    onClick = {
                        displayText = yearLabel
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
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
    val options = listOf("Gender" to "gender", "Kecamatan" to "region")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(140.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable, true),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BpsBlue,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                label = { Text("Tampilan", fontSize = 11.sp) }
            )
            Canvas(modifier = Modifier.matchParentSize().clickable { expanded = !expanded }) {}
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