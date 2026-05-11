package com.zxwork.bmg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zxwork.bmg.ui.detail.SubjectDetailScreen
import com.zxwork.bmg.ui.fav.FavScreen
import com.zxwork.bmg.ui.home.HomeScreen
import com.zxwork.bmg.ui.mine.MineScreen
import com.zxwork.bmg.ui.theme.BmgTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BmgTheme {
                BmgApp()
            }
        }
    }
}

@Composable
fun BmgApp() {
    val navController = rememberNavController()
    val items = listOf(
        AppDestinations.HOME,
        AppDestinations.FAVORITES,
        AppDestinations.PROFILE,
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val showBottomBar = items.any { it.route == currentDestination?.route }

            if (showBottomBar) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // 关键：禁止 Scaffold 自动预留状态栏高度
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.HOME.route,
            // 关键：这里不再使用 padding(innerPadding)，让各页面自行处理 Insets
            modifier = Modifier.fillMaxSize() 
        ) {
            composable(AppDestinations.HOME.route) { 
                HomeScreen(
                    onNavigateToDetail = { id: Int -> 
                        navController.navigate("detail/$id")
                    },
                    // 将底部导航栏的高度传给首页，防止内容被遮挡
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                )
            }
            composable(AppDestinations.FAVORITES.route) { FavScreen() }
            composable(AppDestinations.PROFILE.route) { MineScreen() }
            composable(
                route = "detail/{subjectId}",
                arguments = listOf(navArgument("subjectId") { type = NavType.IntType })
            ) { backStackEntry ->
                SubjectDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

enum class AppDestinations(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("home", "首页", Icons.Default.Home),
    FAVORITES("favorites", "收藏", Icons.Default.Favorite),
    PROFILE("profile", "我的", Icons.Default.AccountBox),
}


@Preview(showBackground = true)
@Composable
fun BmgAppPreview() {
    BmgTheme {
        BmgApp()
    }
}
