package com.example.planeja.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Flag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector? = null
) {
    object Login : Destination("login", "Login")
    object Register : Destination("register", "Cadastro")
    object Home : Destination("home", "Início", Icons.Filled.Home)
    object Metas : Destination("metas", "Metas", Icons.Filled.Flag)
    object Categorias : Destination("categorias", "Categorias", Icons.Filled.Category)
    object Analise : Destination("analise", "Análise", Icons.Filled.BarChart)
    object Ajustes : Destination("ajustes", "Ajustes", Icons.Filled.Settings)
    object NovaTransacao : Destination("novaTransacao", "Nova transação")
    object EditarPerfil : Destination("editar_perfil", "Editar Perfil")

    companion object {
        val bottomItems = listOf(Home, Metas, Categorias, Analise, Ajustes)
    }
}
