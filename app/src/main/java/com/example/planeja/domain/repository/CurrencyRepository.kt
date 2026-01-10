package com.example.planeja.domain.repository

import com.example.planeja.domain.model.Cotacao

interface CurrencyRepository {
    suspend fun obterCotacoesPrincipais(): Result<List<Cotacao>>
}
