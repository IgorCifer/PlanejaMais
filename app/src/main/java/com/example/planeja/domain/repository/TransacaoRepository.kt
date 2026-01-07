package com.example.planeja.domain.repository

import com.example.planeja.data.local.dao.CategoriaTotalDto
import com.example.planeja.domain.model.Transacao
import kotlinx.coroutines.flow.Flow

interface TransacaoRepository {

    fun getTransacoesRecentes(limit: Int): Flow<List<Transacao>>

    fun getTodasTransacoes(): Flow<List<Transacao>>

    suspend fun adicionar(transacao: Transacao)

    suspend fun atualizar(transacao: Transacao)

    suspend fun deletar(transacao: Transacao)
    fun getTotaisDespesasPorCategoria(
        mesStr: String,
        anoStr: String
    ): Flow<List<CategoriaTotalDto>>
}