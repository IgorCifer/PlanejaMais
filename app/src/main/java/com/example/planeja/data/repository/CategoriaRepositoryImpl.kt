package com.example.planeja.data.repository

import com.example.planeja.data.local.dao.CategoriaDao
import com.example.planeja.data.mapper.toDomain
import com.example.planeja.data.mapper.toEntity
import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoriaRepositoryImpl(
    private val dao: CategoriaDao
) : CategoriaRepository {

    override fun getCategorias(): Flow<List<Categoria>> =
        dao.getCategorias().map { list -> list.map { it.toDomain() } }

    override suspend fun criar(categoria: Categoria) {
        dao.inserir(categoria.toEntity())
    }

    override suspend fun atualizar(categoria: Categoria) {
        dao.atualizar(categoria.toEntity())
    }

    override suspend fun deletar(categoria: Categoria) {
        dao.deletar(categoria.toEntity())
    }
}
