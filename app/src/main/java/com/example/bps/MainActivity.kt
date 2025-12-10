package com.example.bps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.bps.components.BottomNavWithMoreMenu
import com.example.bps.components.BpsDrawerContent
import com.example.bps.theme.*
import com.example.bps.ui.beranda.BerandaScreen
import com.example.bps.ui.general.ContentType
import com.example.bps.ui.general.GeneralListScreen
import com.example.bps.ui.infografik.InfografikScreen
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.statistik.DatasetListScreen
import com.example.bps.ui.statistik.StatistikScreen
import com.example.bps.ui.about.AboutScreen
import com.example.bps.ui.statistik.SubjectList.SubjectListScreen
import com.example.bps.ui.statistik.datasetdetail.DatasetDetailScreen
import com.example.bps.ui.statistik.statistikGraph
import androidx.compose.ui.platform.LocalContext
import com.example.bps.utils.launchInAppBrowser
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BpsTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var showNotif by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    // --- State untuk Drawer & Helper ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Memantau rute saat ini untuk logika Judul & Tombol Back
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared ViewModel
    val newsViewModel: NewsViewModel = viewModel()

    // --- LOGIKA JUDUL DINAMIS ---
    val title = when {
        currentRoute == "beranda" -> "Beranda"
        currentRoute == "statistik" -> "Statistik"
        currentRoute == "infografik" -> "Infografik"

        // Jika sedang buka list dataset, ambil nama subject-nya
        currentRoute?.startsWith("dataset_list/") == true -> {
            navBackStackEntry?.arguments?.getString("subjectName") ?: "Daftar Statistik"
        }

        currentRoute?.startsWith("detail_screen/") == true -> "Detail Dataset"
        currentRoute == "all_news" -> "Berita Kegiatan"
        currentRoute == "all_publications" -> "Publikasi"
        currentRoute == "all_brs" -> "Berita Resmi"
        else -> "SILAWET"
    }

    // Cek apakah kita di halaman utama (Beranda/Statistik/Infografik) atau halaman dalam
    val isRootScreen = currentRoute in listOf("beranda", "statistik", "infografik")

    // --- STRUKTUR UTAMA ---
    val context = LocalContext.current
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            BpsDrawerContent(
                onNavigate = { route -> navController.navigate(route) },
                onOpenLink = { url ->
                    launchInAppBrowser(context, url)
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(text = title, maxLines = 1) },

                    // --- LOGIKA ICON KIRI (Menu vs Back) ---
                    navigationIcon = {
                        if (isRootScreen) {
                            // Tampilkan Hamburger Menu
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Gray800)
                            }
                        } else {
                            // Tampilkan Panah Back
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Gray800)
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { showNotif = !showNotif }) {
                            Icon(painterResource(id = R.drawable.ic_bell_24dp), contentDescription = "Notifications")
                            DropdownMenu(
                                expanded = showNotif,
                                onDismissRequest = { showNotif = false },
                                modifier = Modifier.fillMaxWidth().padding(10.dp)
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.pengaturan), color = Gray800, fontSize = 16.sp) },
                                    onClick = { }
                                )
                            }
                        }
                        IconButton(onClick = { showSettings = !showSettings }) {
                            Icon(painterResource(id = R.drawable.ic_settings_24dp), contentDescription = "Settings")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Orange400,
                        scrolledContainerColor = Orange400,
                        titleContentColor = Black,
                        actionIconContentColor = Gray800,
                        navigationIconContentColor = Gray800
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                BottomNavWithMoreMenu(navController = navController, currentRoute = currentRoute)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "beranda",
                modifier = Modifier.padding(innerPadding)
            ) {
                // 1. BERANDA
                composable("beranda") {
                    BerandaScreen(
                        viewModel = newsViewModel,
                        onSeeAllNews = { navController.navigate("all_news") },
                        onNavigateToDetail = { id, type -> navController.navigate("detail_content/$id/$type") },
                        // INI KUNCINYA: Navigasi dinamis saat menu diklik
                        onMenuClick = { route -> navController.navigate(route) }
                    )
                }

                // 2. STATISTIK & INFOGRAFIK
                composable("statistik") { StatistikScreen(navController) }
                statistikGraph(navController)
                composable("infografik") {
                    InfografikScreen(
                        viewModel = newsViewModel,
                        onNavigateToAllNews = { navController.navigate("all_news") }
                    )
                }

                // 3. NAVIGASI DATASET (Dinamis dari Menu)
                // Rute ini menangkap Subject Name (misal: "Kependudukan dan Migrasi")
                composable(
                    route = "dataset_list/{subjectName}",
                    arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val subject = backStackEntry.arguments?.getString("subjectName") ?: "Data"
                    DatasetListScreen(subjectName = subject, navController = navController)
                }

                composable("about_screen") {
                    AboutScreen(navController = navController)
                }

                composable("subject_list/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) {
                    SubjectListScreen(it.arguments?.getString("categoryId") ?: "0", navController)
                }

                composable("detail_screen/{datasetId}", arguments = listOf(navArgument("datasetId") { type = NavType.StringType })) {
                    DatasetDetailScreen(it.arguments?.getString("datasetId") ?: "", navController)
                }

                // 4. NAVIGASI DRAWER (Sidebar)
                composable("all_publications") { GeneralListScreen(navController, newsViewModel, ContentType.PUBLIKASI, "Semua Publikasi") }
                composable("all_brs") { GeneralListScreen(navController, newsViewModel, ContentType.BRS, "Berita Resmi Statistik") }
                composable("all_infografis") { GeneralListScreen(navController, newsViewModel, ContentType.INFOGRAFIS, "Galeri Infografis") }
                composable("all_news") { GeneralListScreen(navController, newsViewModel, ContentType.NEWS, "Berita Kegiatan") }

                // 5. DETAIL KONTEN UMUM
                composable("detail_content/{itemId}/{type}", arguments = listOf(navArgument("itemId") { type = NavType.IntType }, navArgument("type") { type = NavType.StringType })) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                    val typeString = backStackEntry.arguments?.getString("type") ?: "NEWS"
                    val typeEnum = try { com.example.bps.ui.general.ContentType.valueOf(typeString) } catch (e: Exception) { com.example.bps.ui.general.ContentType.NEWS }
                    com.example.bps.ui.general.GeneralDetailScreen(navController, newsViewModel, itemId, typeEnum)
                }
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    BpsTheme {
        MainScreen()
    }
}