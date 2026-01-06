package com.example.planeja.domain.repository

import com.example.planeja.domain.model.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    fun getCategorias(): Flow<List<Categoria>>
    suspend fun criar(categoria: Categoria)
    suspend fun atualizar(categoria: Categoria)
    suspend fun deletar(categoria: Categoria)
}
