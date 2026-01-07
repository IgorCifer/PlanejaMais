package com.example.planeja.ui.navigation

import androidx.compose.ui.platform.LocalContext
import com.example.planeja.PlanejaApp
import com.example.planeja.ui.categorias.CategoriasRoute
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.planeja.ui.home.HomeScreen
import com.example.planeja.ui.metas.MetasScreen
import com.example.planeja.ui.ajustes.AjustesScreen
import com.example.planeja.ui.transacoes.NovaTransacaoScreen
import com.example.planeja.ui.analise.AnaliseRoute

@Composable
fun PlanejaApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val context = LocalContext.current
    val app = context.applicationContext as PlanejaApp

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
            composable(Destination.Home.route) {
                HomeScreen(
                    onVerTodasMetas = { navController.navigate(Destination.Metas.route) },
                    onNovaTransacao = { navController.navigate(Destination.NovaTransacao.route) }
                )
            }
            composable(Destination.Metas.route) { MetasScreen() }
            composable(Destination.Categorias.route) { CategoriasRoute(app) }
            composable(Destination.Analise.route) { AnaliseRoute(app) }
            composable(Destination.Ajustes.route) { AjustesScreen() }

            composable(Destination.NovaTransacao.route) {
                NovaTransacaoScreen(
                    onTransacaoSalva = { navController.popBackStack() },
                    onVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}
