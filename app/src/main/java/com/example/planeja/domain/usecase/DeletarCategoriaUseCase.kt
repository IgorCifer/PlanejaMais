package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.repository.CategoriaRepository

class DeletarCategoriaUseCase(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria) {
        repository.deletar(categoria)
    }
}