package com.example.bps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.navArgument
import com.example.bps.components.BottomNavWithMoreMenu
import com.example.bps.theme.*
import com.example.bps.ui.beranda.BerandaScreen
import com.example.bps.ui.datasetdetail.DatasetDetailScreen
import com.example.bps.ui.common.GeneralListScreen
import com.example.bps.ui.common.ContentType
import com.example.bps.ui.infografik.InfografikScreen
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.statistik.DatasetListScreen
import com.example.bps.ui.statistik.StatistikScreen
import com.example.bps.ui.statistik.SubjectList.SubjectListScreen
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

    // State untuk Drawer dan UriHandler
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current // Untuk membuka link browser/WA

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared ViewModel
    val newsViewModel: NewsViewModel = viewModel()

    val title = when {
        currentRoute == "beranda" -> "Beranda"
        currentRoute == "statistik" -> "Statistik"
        currentRoute == "infografik" -> "Infografik"
        currentRoute?.startsWith("dataset_list/") == true -> "Daftar Statistik"
        currentRoute?.startsWith("detail_screen/") == true -> "Detail Dataset"
        else -> "SILAWET"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // --- HEADER DRAWER ---
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Menu Lengkap",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()

                // --- KELOMPOK 1: ARSIP & DATA ---
                Text(
                    text = "Arsip & Data",
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                NavigationDrawerItem(
                    label = { Text(text = "Publikasi dan Buku") },
                    icon = { Icon(painterResource(id = R.drawable.ic_book_marked_24dp), contentDescription = null) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("all_publications")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(text = "Berita Resmi Statistik") },
                    icon = { Icon(painterResource(id = R.drawable.ic_grafik_24dp), contentDescription = null) }, // Ganti icon jika ada
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("all_brs")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(text = "Galeri Infografis") },
                    icon = { Icon(painterResource(id = R.drawable.ic_pie_chart_24dp), contentDescription = null) }, // Ganti icon jika ada
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("all_infografis")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(text = "Berita Kegiatan") },
                    icon = { Icon(painterResource(id = R.drawable.ic_menu_24dp), contentDescription = null) }, // Ganti icon jika ada
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("all_news")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // --- KELOMPOK 2: LAYANAN & PROFIL ---
                Text(
                    text = "Layanan & Profil",
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Gray
                )

                NavigationDrawerItem(
                    label = { Text(text = "Tentang Aplikasi SILAWET") },
                    icon = { Icon(painterResource(id = R.drawable.ic_feedback), contentDescription = null) }, // Pastikan icon tersedia
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        // Tambahkan navigasi ke about jika sudah ada
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(text = "Website Resmi BPS") },
                    icon = { Icon(painterResource(id = R.drawable.ic_internet_filled), contentDescription = null) }, // Ganti icon globe/internet
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        uriHandler.openUri("https://kebumenkab.bps.go.id")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text(text = "Hubungi Via WhatsApp") },
                    icon = { Icon(painterResource(id = R.drawable.ic_whatsapp_fill), contentDescription = null) }, // Ganti icon WA
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        uriHandler.openUri("https://wa.me/6285179763305")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text(text = title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Gray800
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showNotif = !showNotif }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bell_24dp),
                                contentDescription = "Notifications"
                            )
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
                            Icon(
                                painter = painterResource(id = R.drawable.ic_settings_24dp),
                                contentDescription = "Settings"
                            )
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
                BottomNavWithMoreMenu(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "beranda",
                modifier = Modifier.padding(innerPadding)
            ) {
                // ... (BAGIAN NAVHOST TETAP SAMA SEPERTI SEBELUMNYA) ...
                composable("beranda") {
                    BerandaScreen(
                        viewModel = newsViewModel,
                        onSeeAllNews = { navController.navigate("all_news") },
                        onNavigateToDetail = { id, type -> navController.navigate("detail_content/$id/$type") }
                    )
                }
                composable("statistik") { StatistikScreen(navController) }
                composable("infografik") {
                    InfografikScreen(
                        viewModel = newsViewModel,
                        onNavigateToAllNews = { navController.navigate("all_news") }
                    )
                }

                composable("subject_list/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) {
                    SubjectListScreen(it.arguments?.getString("categoryId") ?: "0", navController)
                }
                composable("dataset_list/{subjectName}", arguments = listOf(navArgument("subjectName") { type = NavType.StringType })) {
                    DatasetListScreen(it.arguments?.getString("subjectName") ?: "", navController)
                }
                composable("detail_screen/{datasetId}", arguments = listOf(navArgument("datasetId") { type = NavType.StringType })) {
                    DatasetDetailScreen(it.arguments?.getString("datasetId") ?: "", navController)
                }

                composable("all_publications") {
                    GeneralListScreen(navController, newsViewModel, ContentType.PUBLIKASI, "Semua Publikasi")
                }
                composable("all_brs") {
                    GeneralListScreen(navController, newsViewModel, ContentType.BRS, "Berita Resmi Statistik")
                }
                composable("all_infografis") {
                    GeneralListScreen(navController, newsViewModel, ContentType.INFOGRAFIS, "Galeri Infografis")
                }
                composable("all_news") {
                    GeneralListScreen(navController, newsViewModel, ContentType.NEWS, "Berita Kegiatan")
                }

                composable("detail_content/{itemId}/{type}", arguments = listOf(navArgument("itemId") { type = NavType.IntType }, navArgument("type") { type = NavType.StringType })) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                    val typeString = backStackEntry.arguments?.getString("type") ?: "NEWS"
                    val typeEnum = try { com.example.bps.ui.common.ContentType.valueOf(typeString) } catch (e: Exception) { com.example.bps.ui.common.ContentType.NEWS }
                    com.example.bps.ui.common.GeneralDetailScreen(navController, newsViewModel, itemId, typeEnum)
                }
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    BpsTheme {
        MainScreen()
    }
}