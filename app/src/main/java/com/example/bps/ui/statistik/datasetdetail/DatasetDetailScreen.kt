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
import com.example.bps.theme.*
import com.example.bps.components.BpsChildTopBar

// Warna Biru BPS untuk Tab Aktif
val BpsBlue = Color(0xFF0D47A1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatasetDetailScreen(
    datasetId: String, // String dari Navigasi
//    subjectName: String,
    navController: NavController,
    viewModel: DetailDatasetViewModel = viewModel()
) {
    val uiState by viewModel.uiState

    // 0 = Tabel Data, 1 = Chart
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Tabel Data", "Chart")

    // Konversi ID ke Int karena API butuh Int
    val idAsInt = datasetId.toIntOrNull() ?: 0

    // Load data awal
    LaunchedEffect(idAsInt) {
        if (idAsInt != 0 && uiState.dataset == null) {
            viewModel.getDatasetDetail(idAsInt)
        }
    }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text("Detail Dataset", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Orange400),
//
//                modifier = Modifier.height(56.dp)
//            )
//        }
        topBar = {
            // PANGGIL KOMPONEN YANG KITA BUAT TADI
            BpsChildTopBar(
                title = "Detail Dataset", // <--- Ganti variable error tadi dengan Teks ini
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Gagal memuat: ${uiState.error}", color = Color.Red)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.getDatasetDetail(idAsInt) }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                uiState.dataset != null -> {
                    // Ambil data (Tipe: DatasetDetailData)
                    val data = uiState.dataset!!

                    // --- LOGIKA TAHUN (CamelCase Updated) ---
                    val sortedYears = remember(data.availableYears) {
                        data.availableYears.sortedDescending()
                    }

                    // Tentukan tahun aktif (CamelCase Updated)
                    // Logic: Jika currentYear 0 atau null, ambil tahun terbesar
                    val activeYear = if (data.currentYear != 0) data.currentYear else (data.availableYears.maxOrNull() ?: 0)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // --- HEADER & FILTER ---
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Property: datasetName (CamelCase)
                            Text(
                                text = data.dataset.datasetName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically // Pastikan rata tengah secara vertikal
                            ) {
                                // A. Sumber
                                Text(
                                    text = "Sumber: ${data.dataset.source}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )

                                // B. Logic Unit (Di dalam Row yang sama)
                                if (!data.unit.isNullOrEmpty()) {
                                    // Jarak Horizontal (Berfungsi karena di dalam Row)
                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Titik pemisah
                                    Text(text = "•", fontSize = 12.sp, color = Color.Gray)

                                    // Jarak lagi
                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Satuan
                                    Text(
                                        text = "Satuan: ${data.unit}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF009688), // Hijau Teal
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Property: availableYears (CamelCase)
                            if (data.availableYears.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // 1. DROPDOWN TAHUN
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Tahun: ", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                        YearDropdown(
                                            years = sortedYears,
                                            selectedYear = activeYear,
                                            onYearSelected = { newYear ->
                                                viewModel.getDatasetDetail(idAsInt, newYear)
                                            }
                                        )
                                    }

                                    // 2. DROPDOWN MODE (Logic String Contains)
                                    // Menggunakan CamelCase: datasetName
                                    val isPopulationData = data.dataset.datasetName.contains("Penduduk", ignoreCase = true) &&
                                            data.dataset.datasetName.contains("Kecamatan", ignoreCase = true)

                                    if (isPopulationData) {
                                        ModeDropdown(onModeSelected = { selectedMode ->
                                            viewModel.getDatasetDetail(idAsInt, activeYear, selectedMode)
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
                                    text = {
                                        Text(title, color = if (selectedTabIndex == index) BpsBlue else Color.Gray)
                                    }
                                )
                            }
                        }

                        // --- CONTENT ---
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            when (selectedTabIndex) {
                                0 -> { // TABEL DATA
                                    Column {
                                        Text("Rincian Data Tabel", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))

                                        // --- FILTER TABEL ---
                                        val currentYearStr = activeYear.toString()

                                        // Property: data.table.headers
                                        val filteredHeaders = data.table.headers.filterIndexed { index, header ->
                                            // Menjaga header pertama (Wilayah) ATAU header yang mengandung tahun aktif
                                            // Handle case: "2024.0" (format excel number) vs "2024"
                                            index == 0 || header.substringBefore(".") == currentYearStr
                                        }

                                        val filteredTableData = data.table.copy(headers = filteredHeaders)

                                        // Render Tabel (Pastikan parameter TabelDataSection sesuai DataClass TableData)
                                        if (filteredHeaders.size >= 2) {
                                            TabelDataSection(tableData = filteredTableData)
                                        } else {
                                            // Fallback jika filter gagal (tampilkan semua atau kosong)
                                            TabelDataSection(tableData = data.table)
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text("Menampilkan data tahun $activeYear.", fontSize = 12.sp, color = Color.Gray, fontStyle = FontStyle.Italic)
                                    }
                                }
                                1 -> { // CHART
                                    Column {
                                        // Pengecekan Chart (data.chart tidak null di DataClass, tapi isinya bisa null/empty)
                                        // Kita cek apakah ada labels/data untuk ditampilkan
                                        val isChartAvailable = data.chart.labels.isNotEmpty()

                                        if (isChartAvailable) {
                                            Text("Visualisasi Data", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                                            ChartSection(chartData = data.chart)
                                            Spacer(modifier = Modifier.height(24.dp))
                                        } else {
                                            Text("Grafik tidak tersedia untuk dataset ini.", color = Color.Gray, fontStyle = FontStyle.Italic)
                                            Spacer(modifier = Modifier.height(16.dp))
                                        }

                                        // Property: insights (CamelCase)
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