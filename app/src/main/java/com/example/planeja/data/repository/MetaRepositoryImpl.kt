package com.example.planeja.data.repository

import com.example.planeja.data.local.dao.MetaDao
import com.example.planeja.data.mapper.toDomain
import com.example.planeja.data.mapper.toEntity
import com.example.planeja.domain.model.Meta
import com.example.planeja.domain.repository.MetaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MetaRepositoryImpl(
    private val dao: MetaDao
) : MetaRepository {

    override fun getMetas(): Flow<List<Meta>> =
        dao.getMetas().map { list -> list.map { it.toDomain() } }

    override fun getMetasHome(limit: Int): Flow<List<Meta>> =
        dao.getMetasLimit(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun criar(meta: Meta) {
        dao.insert(meta.toEntity())
    }

    override suspend fun atualizar(meta: Meta) {
        dao.update(meta.toEntity())
    }

    override suspend fun deletar(meta: Meta) {
        dao.delete(meta.toEntity())
    }
}
