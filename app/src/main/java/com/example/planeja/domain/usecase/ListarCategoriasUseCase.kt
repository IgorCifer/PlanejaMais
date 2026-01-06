package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow

class ListarCategoriasUseCase(
    private val repository: CategoriaRepository
) {
    operator fun invoke(): Flow<List<Categoria>> = repository.getCategorias()
}