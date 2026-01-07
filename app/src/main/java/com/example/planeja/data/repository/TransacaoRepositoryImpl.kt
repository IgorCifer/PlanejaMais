package com.example.planeja.data.repository

import com.example.planeja.data.local.dao.TransacaoDao
import com.example.planeja.data.mapper.toDomain
import com.example.planeja.data.mapper.toEntity
import com.example.planeja.domain.model.Transacao
import com.example.planeja.data.local.dao.CategoriaTotalDto
import com.example.planeja.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransacaoRepositoryImpl(
    private val dao: TransacaoDao
) : TransacaoRepository {

    override fun getTransacoesRecentes(limit: Int): Flow<List<Transacao>> =
        dao.getRecentes(limit).map { list -> list.map { it.toDomain() } }

    override fun getTodasTransacoes(): Flow<List<Transacao>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun adicionar(transacao: Transacao) {
        dao.insert(transacao.toEntity())
    }

    override suspend fun atualizar(transacao: Transacao) {
        dao.update(transacao.toEntity())
    }

    override suspend fun deletar(transacao: Transacao) {
        dao.delete(transacao.toEntity())
    }
    override fun getTotaisDespesasPorCategoria(
        mesStr: String,
        anoStr: String
    ): Flow<List<CategoriaTotalDto>> =
        dao.getTotaisDespesasPorCategoria(mesStr, anoStr)
}
