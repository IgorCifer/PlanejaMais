package com.example.planeja.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.planeja.MainActivity
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.permission.NotificationPermissionManager
import com.example.planeja.ui.ajustes.AjustesScreen
import com.example.planeja.ui.ajustes.EditarPerfilScreen
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
fun PlanejaApp(
    authViewModel: AuthViewModel,
    permissionManager: NotificationPermissionManager,
    activity: MainActivity,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val context = LocalContext.current
    val app = context.applicationContext as PlanejaApp

    val currentUser by authViewModel.currentUser.collectAsState()


    var lastUserId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(currentUser?.id) {
        val newId = currentUser?.id
        if (newId == lastUserId) return@LaunchedEffect
        lastUserId = newId

        val targetRoute = if (currentUser == null) {
            Destination.Login.route
        } else {
            Destination.Home.route
        }

        navController.navigate(targetRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    val isAuthRoute = currentRoute == Destination.Login.route ||
            currentRoute == Destination.Register.route


    Scaffold(
        bottomBar = {
            if (!isAuthRoute && currentUser != null) {
                val colorScheme = MaterialTheme.colorScheme
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = colorScheme.onSurface,
                    tonalElevation = 4.dp
                ) {
                    Destination.bottomItems.forEach { dest ->
                        val selected = currentRoute == dest.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                dest.icon?.let { icon ->
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = dest.label,
                                        tint = if (selected)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = dest.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            )
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
                    userName = currentUser?.name ?: "Usuário",
                    onVerTodasMetas = { navController.navigate(Destination.Metas.route) },
                    onNovaTransacao = { navController.navigate(Destination.NovaTransacao.route) },
                    onEditarTransacao = { id ->
                        navController.navigate("novaTransacao/$id")
                    }
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
                    notificationRepository = app.container.notificationRepository,
                    permissionManager = permissionManager,
                    activity = activity,
                    authViewModel = authViewModel,
                    onNavigateToEditProfile = {
                        navController.navigate(Destination.EditarPerfil.route)
                    }
                )
            }

            composable(Destination.EditarPerfil.route) {
                EditarPerfilScreen(
                    authViewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Destination.NovaTransacao.route) {
                NovaTransacaoScreen(
                    transacaoId = null,
                    onTransacaoSalva = { navController.popBackStack() },
                    onVoltar = { navController.popBackStack() }
                )
            }

            composable(
                route = "novaTransacao/{transacaoId}",
                arguments = listOf(
                    navArgument("transacaoId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("transacaoId")
                NovaTransacaoScreen(
                    transacaoId = id,
                    onTransacaoSalva = { navController.popBackStack() },
                    onVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}
