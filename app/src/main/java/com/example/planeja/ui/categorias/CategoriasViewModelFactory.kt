package com.example.planeja.ui.categorias

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp

object CategoriasViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            CategoriasViewModel(
                listarCategorias = app.container.listarCategoriasUseCase,
                criarCategoria = app.container.criarCategoriaUseCase,
                atualizarCategoria = app.container.atualizarCategoriaUseCase,
                deletarCategoria = app.container.deletarCategoriaUseCase
            )
        }
    }
}
