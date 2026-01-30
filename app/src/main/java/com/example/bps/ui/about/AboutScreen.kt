package com.example.bps.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bps.R
import com.example.bps.components.BpsChildTopBar
import com.example.bps.components.MedsosFooter
import com.example.bps.theme.BpsTheme
import com.example.bps.theme.Orange500

// --- ENUM UNTUK MENENTUKAN ISI SHEET ---
enum class AboutSheetType {
    NONE, BPS_INFO, UPB_INFO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 1. STATE UNTUK BOTTOM SHEET
    var activeSheet by remember { mutableStateOf(AboutSheetType.NONE) }

    // State khusus untuk mengontrol sheet agar animasi tutupnya halus
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Helper function untuk membuka sheet
    fun openSheet(type: AboutSheetType) {
        activeSheet = type
        showBottomSheet = true
    }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Tentang Aplikasi") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.White,
//                    titleContentColor = Color.Black,
//                    navigationIconContentColor = Color.Black
//                )
//            )
//        }
        topBar = {
            // PANGGIL KOMPONEN YANG KITA BUAT TADI
            BpsChildTopBar(
                title = "Tentang Aplikasi", // <--- Ganti variable error tadi dengan Teks ini
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... (Bagian Logo SILAWET sama seperti sebelumnya) ...
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo SILAWET",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("SILAWET", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Orange500)
            Text("Sistem Layanan Wawasan E-Statistik", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text("Versi ${com.example.bps.BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
            Spacer(modifier = Modifier.height(32.dp))

            // Deskripsi (Sama)
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("SILAWET adalah aplikasi resmi dari BPS Kabupaten Kebumen yang dirancang untuk memudahkan masyarakat dalam mengakses dan memahami data statistik Kabupaten Kebumen. Dapatkan wawasan mendalam melalui visualisasi data yang interaktif dan informatif.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- BAGIAN KERJA SAMA (DENGAN CLICKABLE) ---
            Text("Kerja Sama", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Aplikasi ini dikembangkan atas kerja sama antara BPS Kabupaten Kebumen dengan Program Studi S1 Sains Data Universitas Putra Bangsa.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2. LOGO BPS (DITAMBAHKAN KLIK)
                Image(
                    painter = painterResource(id = R.drawable.logobps),
                    contentDescription = "Logo BPS",
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                        .clickable { openSheet(AboutSheetType.BPS_INFO) }, // KLIK DISINI
                    contentScale = ContentScale.Fit
                )

                // 3. LOGO UPB (DITAMBAHKAN KLIK)
                Image(
                    painter = painterResource(id = R.drawable.logosainsdata),
                    contentDescription = "Logo Program Studi Sains Data",
                    modifier = Modifier
                        .height(60.dp)
                        .weight(1f)
                        .clickable { openSheet(AboutSheetType.UPB_INFO) }, // KLIK DISINI
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("(Klik logo untuk info tim)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // ... (Bagian Kontak & Medsos tetap sama) ...
            Text("Hubungi Kami", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            ContactRow(Icons.Default.LocationOn, "Jl. Arungbinang No. 9, Kebumen") { /* Intent Maps */ }
            ContactRow(Icons.Default.Email, "bps3305@bps.go.id") { /* Intent Email */ }

            Spacer(modifier = Modifier.height(24.dp))
            MedsosFooter()
            Spacer(modifier = Modifier.height(24.dp))
            Image(painter = painterResource(id = R.drawable.berakhlak), contentDescription = null, modifier = Modifier.fillMaxWidth().height(80.dp), contentScale = ContentScale.Fit)
            Spacer(modifier = Modifier.height(16.dp))
            Text("© ${java.time.Year.now()} BPS Kabupaten Kebumen", style = MaterialTheme.typography.labelMedium, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
        }

        // 4. MODAL BOTTOM SHEET
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                // Isi konten berdasarkan tipe yang aktif
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 48.dp) // Padding bawah agak besar
                ) {
                    when (activeSheet) {
                        AboutSheetType.BPS_INFO -> {
                            HeaderSheet("Tim BPS Kabupaten Kebumen")
                            RoleItem("Penanggung Jawab", "Danisworo, S.Si., M.Si")
                            RoleItem("Pengarah", "Mulyo Widodo, S.ST, M.Si")
                            RoleItem("Tim Teknis", "Tim IPDS BPS Kab. Kebumen")
                        }
                        AboutSheetType.UPB_INFO -> {
                            HeaderSheet("Tim Pengembang Sainsa Data UPB")
                            RoleItem("Project Manager", "Andi Riawan") // Ganti nama jika ada
                            RoleItem("UI/UX Designer", "Radita Pinardi")
                            RoleItem("Front End Mobile", "Evan Alif Widhyatma")
                            RoleItem("Back End Developer", "Faiz Zamzami")
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

// --- KOMPONEN PENDUKUNG SHEET ---

@Composable
fun HeaderSheet(title: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Orange500
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun RoleItem(role: String, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = role,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.DarkGray,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

// ... (Kode ContactRow tetap sama) ...
@Composable
fun ContactRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    // ... sama seperti kode sebelumnya ...
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Orange500, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
    HorizontalDivider(color = Color.LightGray.copy(0.3f))
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    BpsTheme {
        AboutScreen(navController = rememberNavController())
    }
}