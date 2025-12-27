package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.repository.TransacaoRepository

class AdicionarTransacaoUseCase(
    private val repository: TransacaoRepository
) {
    suspend operator fun invoke(transacao: Transacao) {
        repository.adicionar(transacao)
    }
}
