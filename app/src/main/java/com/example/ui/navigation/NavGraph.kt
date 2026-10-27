package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.repository.FitLifeRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TermsScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.YogaScreen
import com.example.ui.theme.PrimarySaffron

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Nutrition : Screen("nutrition", "Nutrition", Icons.Default.Restaurant)
    data object Yoga : Screen("yoga", "Yoga", Icons.Default.SelfImprovement)
    data object Tools : Screen("tools", "Tools", Icons.Default.Build)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
    data object Terms : Screen("terms", "Terms", Icons.Default.Build)
}

val BottomNavItems = listOf(
    Screen.Home,
    Screen.Nutrition,
    Screen.Yoga,
    Screen.Tools,
    Screen.Profile
)

@Composable
fun MainAppNavigation(
    repository: FitLifeRepository,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in BottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp
                ) {
                    BottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = screen.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimarySaffron,
                                selectedTextColor = PrimarySaffron,
                                indicatorColor = PrimarySaffron.copy(alpha = 0.15f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    onNavigateToNutrition = { navController.navigate(Screen.Nutrition.route) },
                    onNavigateToYoga = { navController.navigate(Screen.Yoga.route) },
                    onNavigateToTerms = { navController.navigate(Screen.Terms.route) }
                )
            }
            composable(Screen.Nutrition.route) {
                NutritionScreen(repository = repository)
            }
            composable(Screen.Yoga.route) {
                YogaScreen(repository = repository)
            }
            composable(Screen.Tools.route) {
                ToolsScreen(
                    repository = repository,
                    onNavigateToHome = { navController.navigate(Screen.Home.route) },
                    onNavigateToNutrition = { navController.navigate(Screen.Nutrition.route) },
                    onNavigateToYoga = { navController.navigate(Screen.Yoga.route) }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    repository = repository,
                    onNavigateToTerms = { navController.navigate(Screen.Terms.route) }
                )
            }
            composable(Screen.Terms.route) {
                TermsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
