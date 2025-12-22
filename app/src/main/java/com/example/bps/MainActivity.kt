package com.example.bps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.bps.components.BottomNavWithMoreMenu
import com.example.bps.components.BpsDrawerContent
import com.example.bps.theme.*
import com.example.bps.ui.beranda.BerandaScreen
import com.example.bps.ui.beranda.IndicatorViewModel
import com.example.bps.ui.general.ContentType
import com.example.bps.ui.general.GeneralListScreen
import com.example.bps.ui.infografik.InfografikScreen
import com.example.bps.ui.infografik.news.NewsViewModel
import com.example.bps.ui.statistik.DatasetListScreen
import com.example.bps.ui.statistik.StatistikScreen
import com.example.bps.ui.about.AboutScreen
import com.example.bps.ui.statistik.subjectlist.SubjectListScreen
import com.example.bps.ui.statistik.datasetdetail.DatasetDetailScreen
import com.example.bps.ui.statistik.statistikGraph
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
    // Variabel state untuk menu dropdown dihapus sementara karena tidak dipakai
    // var showNotif by remember { mutableStateOf(false) }
    // var showSettings by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val newsViewModel: NewsViewModel = viewModel()
    val indicatorViewModel: IndicatorViewModel = viewModel()

    val title = when {
        currentRoute == "beranda" -> "Beranda"
        currentRoute == "statistik" -> "Statistik"
        currentRoute == "infografik" -> "Infografik"
        currentRoute?.startsWith("dataset_list/") == true -> {
            navBackStackEntry?.arguments?.getString("subjectName") ?: "Daftar Statistik"
        }
        currentRoute?.startsWith("detail_screen/") == true -> "Detail Dataset"
        currentRoute == "all_news" -> "Berita Kegiatan"
        currentRoute == "all_publications" -> "Publikasi"
        currentRoute == "all_brs" -> "Berita Resmi"
        else -> "SILAWET"
    }

    val isRootScreen = currentRoute in listOf("beranda", "statistik", "infografik")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            BpsDrawerContent(
                onNavigate = { route -> navController.navigate(route) },
                onOpenLink = { url -> launchInAppBrowser(context, url) },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            topBar = {
                if (isRootScreen) {
                    TopAppBar(
                        title = { Text(text = title, maxLines = 1) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Gray800)
                            }
                        },
                        // --- PERUBAHAN DI SINI: ACTIONS DIKOSONGKAN ---
                        actions = {
                            // Tombol notifikasi dan settings dihapus sementara
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
                }
            },

            bottomBar = {
                if (isRootScreen) {
                    BottomNavWithMoreMenu(navController = navController, currentRoute = currentRoute)
                }
            }

        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "beranda",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("beranda") {
                    BerandaScreen(
                        newsViewModel = newsViewModel,
                        indicatorViewModel = indicatorViewModel,
                        onSeeAllNews = { navController.navigate("all_news") },
                        onNavigateToDetail = { id, type -> navController.navigate("detail_content/$id/$type") },
                        onMenuClick = { slug ->
                            if (slug == "others" || slug == "lainnya") {
                                try {
                                    val phoneNumber = "62895422891969"
                                    val message = "Halo BPS Kebumen..."
                                    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(url)
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) { e.printStackTrace() }
                            } else {
                                navController.navigate("menu_grid/$slug")
                            }
                        }
                    )
                }

                composable(
                    route = "menu_grid/{slug}",
                    arguments = listOf(navArgument("slug") { type = NavType.StringType })
                ) { backStackEntry ->
                    val slug = backStackEntry.arguments?.getString("slug") ?: "others"
                    com.example.bps.ui.statistik.MenuGridScreen(slug = slug, navController = navController)
                }

                composable("statistik") { StatistikScreen(navController) }
                statistikGraph(navController)
                composable("infografik") {
                    InfografikScreen(
                        viewModel = newsViewModel,
                        onNavigateToAllNews = { navController.navigate("all_news") },
                        onNavigateToDetail = { id, type ->
                            navController.navigate("detail_content/$id/$type")
                        }
                    )
                }

                composable(
                    route = "dataset_list/{subjectName}",
                    arguments = listOf(navArgument("subjectName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val subject = backStackEntry.arguments?.getString("subjectName") ?: "Data"
                    DatasetListScreen(subjectName = subject, navController = navController)
                }

                composable(
                    route = "detail_screen/{datasetId}",
                    arguments = listOf(navArgument("datasetId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val idYangDitangkap = backStackEntry.arguments?.getString("datasetId") ?: ""
                    DatasetDetailScreen(
                        datasetId = idYangDitangkap,
                        navController = navController
                    )
                }

                composable("about_screen") { AboutScreen(navController = navController) }

                composable("subject_list/{categoryId}", arguments = listOf(navArgument("categoryId") { type = NavType.StringType })) {
                    SubjectListScreen(it.arguments?.getString("categoryId") ?: "0", navController)
                }

                composable("all_publications") { GeneralListScreen(navController, newsViewModel, ContentType.PUBLIKASI, "Semua Publikasi") }
                composable("all_brs") { GeneralListScreen(navController, newsViewModel, ContentType.BRS, "Berita Resmi Statistik") }
                composable("all_infografis") { GeneralListScreen(navController, newsViewModel, ContentType.INFOGRAFIS, "Galeri Infografis") }
                composable("all_news") { GeneralListScreen(navController, newsViewModel, ContentType.NEWS, "Berita Kegiatan") }

                composable("detail_content/{itemId}/{type}", arguments = listOf(navArgument("itemId") { type = NavType.IntType }, navArgument("type") { type = NavType.StringType })) { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                    val typeString = backStackEntry.arguments?.getString("type") ?: "NEWS"
                    val typeEnum = ContentType.entries.find { it.name == typeString } ?: ContentType.NEWS
                    com.example.bps.ui.general.GeneralDetailScreen(navController, newsViewModel, itemId, typeEnum)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    BpsTheme {
        MainScreen()
    }
}