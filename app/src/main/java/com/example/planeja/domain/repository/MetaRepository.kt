package com.example.planeja.domain.repository

import com.example.planeja.domain.model.Meta
import kotlinx.coroutines.flow.Flow

interface MetaRepository {

    fun getMetas(): Flow<List<Meta>>

    fun getMetasHome(limit: Int): Flow<List<Meta>>

    suspend fun criar(meta: Meta)

    suspend fun atualizar(meta: Meta)

    suspend fun deletar(meta: Meta)
}