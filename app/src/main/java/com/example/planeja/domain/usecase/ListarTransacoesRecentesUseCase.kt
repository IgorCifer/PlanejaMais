package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.Flow

class ListarTransacoesRecentesUseCase(
    private val repository: TransacaoRepository
) {
    operator fun invoke(limit: Int): Flow<List<Transacao>> {
        return repository.getTransacoesRecentes(limit)
    }
}
