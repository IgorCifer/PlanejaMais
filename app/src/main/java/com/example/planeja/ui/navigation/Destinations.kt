package com.example.planeja.ui.navigation

sealed class Destination(val route: String, val label: String) {
    object Home : Destination("home", "Início")
    object Metas : Destination("metas", "Metas")
    object Categorias : Destination("categorias", "Categorias")
    object Analise : Destination("analise", "Análise")
    object Ajustes : Destination("ajustes", "Ajustes")

    companion object {
        val bottomItems = listOf(Home, Metas, Categorias, Analise, Ajustes)
    }
}
