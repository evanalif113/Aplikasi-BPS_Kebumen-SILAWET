package com.example.bps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.bps.ui.maps.MapsScreen
import com.example.bps.ui.statistik.DatasetListScreen
import com.example.bps.ui.statistik.StatistikScreen
import com.example.bps.ui.statistik.SubjectList.SubjectListScreen
import androidx.navigation.navArgument
import com.example.bps.ui.common.GeneralDetailScreen
import com.example.bps.ui.common.GeneralListScreen

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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Shared ViewModel untuk news
    val newsViewModel: NewsViewModel = viewModel()

    val title = when {
        currentRoute == "beranda" -> "Beranda"
        currentRoute == "statistik" -> "Statistik"
        currentRoute == "maps" -> "Peta"
        currentRoute == "infografik" -> "Infografik"
        currentRoute?.startsWith("dataset_list/") == true -> "Daftar Statistik"
        currentRoute?.startsWith("detail_screen/") == true -> "Detail Dataset"
        else -> "SILAWET"
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
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
                    containerColor = Orange300,
                    titleContentColor = Black,
                    actionIconContentColor = Gray800
                ),
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavWithMoreMenu(
                navController = navController,
                currentRoute = currentRoute // <-- Berikan route yang aktif
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "beranda",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Layar Utama
            composable("beranda") {
                BerandaScreen(
                    viewModel = newsViewModel,
                    onSeeAllNews = {
                        navController.navigate("all_news")
                    },
                    onNavigateToDetail = { id, type ->
                        navController.navigate("detail_content/$id/$type")
                    }
                )
            }
            composable("statistik") {
                StatistikScreen(navController)
            }
            composable("maps") {
                MapsScreen()
            }
            composable("infografik") {
                InfografikScreen(
                    viewModel = newsViewModel,
                    onNavigateToAllNews = {
                        navController.navigate("all_news") }
                )
            }

            // Layar Statistik: Subject List
            composable(
                route = "subject_list/{categoryId}",
                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "0"
                SubjectListScreen(categoryId = categoryId, navController = navController)
            }

            // Layar Statistik: Dataset List
            composable(
                route = "dataset_list/{subjectName}",
                arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
            ) { backStackEntry ->
                val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
                DatasetListScreen(subjectName = subjectName, navController = navController)
            }

            // Layar Dataset Detail
            composable(
                route = "detail_screen/{datasetId}",
                arguments = listOf(navArgument("datasetId") { type = NavType.StringType })
            ) { backStackEntry ->
                val datasetId = backStackEntry.arguments?.getString("datasetId") ?: ""
                DatasetDetailScreen(datasetId = datasetId, navController = navController)
            }
            //Route Menu Lainnya
            // 1. PUBLIKASI (BUKU)
            composable("all_publications") {
                GeneralListScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    contentType = ContentType.PUBLIKASI,
                    title = "Semua Publikasi"
                )
            }

            // 2. BRS (BERITA RESMI)
            composable("all_brs") {
                GeneralListScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    contentType = ContentType.BRS,
                    title = "Berita Resmi Statistik"
                )
            }

            // 3. INFOGRAFIS
            composable("all_infografis") {
                GeneralListScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    contentType = ContentType.INFOGRAFIS,
                    title = "Galeri Infografis"
                )
            }

            // 4. BERITA KEGIATAN
            composable("all_news") {
                GeneralListScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    contentType = ContentType.NEWS,
                    title = "Berita Kegiatan"
                )
            }

            // --- TAMBAHKAN INI: RUTE HALAMAN DETAIL ---
            composable(
                route = "detail_content/{itemId}/{type}",
                arguments = listOf(
                    navArgument("itemId") { type = NavType.IntType },
                    navArgument("type") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                // 1. Ambil Argumen dari URL
                val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                val typeString = backStackEntry.arguments?.getString("type") ?: "NEWS"

                // 2. Konversi String ke Enum ContentType
                val typeEnum = try {
                    com.example.bps.ui.common.ContentType.valueOf(typeString)
                } catch (e: Exception) {
                    com.example.bps.ui.common.ContentType.NEWS
                }

                // 3. Panggil Layar Detail
                com.example.bps.ui.common.GeneralDetailScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    id = itemId,
                    contentType = typeEnum
                )
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    BpsTheme { MainScreen() }
}
