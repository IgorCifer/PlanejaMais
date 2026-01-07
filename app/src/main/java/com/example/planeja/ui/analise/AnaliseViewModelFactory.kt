package com.example.planeja.ui.analise

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp

object AnaliseViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            AnaliseViewModel(
                obterResumoPorCategoria = app.container.obterResumoPorCategoriaUseCase
            )
        }
    }
}
