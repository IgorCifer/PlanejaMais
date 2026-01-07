package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.repository.CategoriaRepository

class CriarCategoriaUseCase(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria) {
        repository.criar(categoria)
    }
}