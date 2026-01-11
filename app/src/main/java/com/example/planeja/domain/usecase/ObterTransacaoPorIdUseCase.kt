package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObterTransacaoPorIdUseCase(
    private val repository: TransacaoRepository
) {
    operator fun invoke(id: Long): Flow<Transacao?> {
        return repository.getTodasTransacoes()
            .map { lista -> lista.firstOrNull { it.id == id } }
    }
}