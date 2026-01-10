package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Cotacao
import com.example.planeja.domain.repository.CurrencyRepository

class ObterCotacoesPrincipaisUseCase(
    private val repository: CurrencyRepository
) {
    suspend operator fun invoke(): Result<List<Cotacao>> {
        return repository.obterCotacoesPrincipais()
    }
}