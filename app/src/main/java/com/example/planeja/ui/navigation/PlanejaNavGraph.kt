package com.example.planeja.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.planeja.PlanejaApp
import com.example.planeja.ui.ajustes.AjustesScreen
import com.example.planeja.ui.analise.AnaliseRoute
import com.example.planeja.ui.auth.AuthViewModel
import com.example.planeja.ui.auth.LoginScreen
import com.example.planeja.ui.auth.RegisterScreen
import com.example.planeja.ui.categorias.CategoriasRoute
import com.example.planeja.ui.home.HomeScreen
import com.example.planeja.ui.metas.MetasScreen
import com.example.planeja.ui.transacoes.NovaTransacaoScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlanejaApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val context = LocalContext.current
    val app = context.applicationContext as PlanejaApp

    val currentUser by authViewModel.currentUser.collectAsState()


    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate(Destination.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(Destination.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val isAuthRoute = currentRoute == Destination.Login.route ||
            currentRoute == Destination.Register.route

    Scaffold(
        bottomBar = {
            if (!isAuthRoute && currentUser != null) {
                NavigationBar {
                    Destination.bottomItems.forEach { dest ->
                        NavigationBarItem(
                            selected = currentRoute == dest.route,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Rotas de autenticação
            composable(Destination.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                    },
                    onNavigateToRegister = {
                        navController.navigate(Destination.Register.route)
                    }
                )
            }

            composable(Destination.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Destination.Home.route) {
                HomeScreen(
                    onVerTodasMetas = { navController.navigate(Destination.Metas.route) },
                    onNovaTransacao = { navController.navigate(Destination.NovaTransacao.route) }
                )
            }

            composable(Destination.Metas.route) {
                MetasScreen()
            }

            composable(Destination.Categorias.route) {
                CategoriasRoute(app)
            }

            composable(Destination.Analise.route) {
                AnaliseRoute(app)
            }

            composable(Destination.Ajustes.route) {
                AjustesScreen(
                    onLogout = { authViewModel.logout() },
                    notificationRepository = app.container.notificationRepository
                )
            }

            composable(Destination.NovaTransacao.route) {
                NovaTransacaoScreen(
                    onTransacaoSalva = { navController.popBackStack() },
                    onVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}
