package com.example.planeja.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.planeja.ui.home.HomeScreen
import com.example.planeja.ui.metas.MetasScreen
import com.example.planeja.ui.categorias.CategoriasScreen
import com.example.planeja.ui.analise.AnaliseScreen
import com.example.planeja.ui.ajustes.AjustesScreen

@Composable
fun PlanejaApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                Destination.bottomItems.forEach { dest ->
                    NavigationBarItem(
                        selected = currentRoute == dest.route,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { /* depois adiciona Icon() */ },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) { HomeScreen() }
            composable(Destination.Metas.route) { MetasScreen() }
            composable(Destination.Categorias.route) { CategoriasScreen() }
            composable(Destination.Analise.route) { AnaliseScreen() }
            composable(Destination.Ajustes.route) { AjustesScreen() }
        }
    }
}
