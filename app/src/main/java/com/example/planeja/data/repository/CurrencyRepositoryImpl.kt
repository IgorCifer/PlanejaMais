package com.example.planeja.data.repository

import android.util.Log
import com.example.planeja.data.remote.api.FrankfurterApi
import com.example.planeja.domain.model.Cotacao
import com.example.planeja.domain.repository.CurrencyRepository

class CurrencyRepositoryImpl(
    private val api: FrankfurterApi
) : CurrencyRepository {

    private var cachedRates: List<Cotacao>? = null
    private var lastFetchTimeMillis: Long = 0L
    private val cacheDurationMillis = 60_000L

    override suspend fun obterCotacoesPrincipais(): Result<List<Cotacao>> {
        val now = System.currentTimeMillis()

        cachedRates?.let { cache ->
            if (now - lastFetchTimeMillis < cacheDurationMillis) {
                return Result.success(cache)
            }
        }

        return try {
            val response = api.getLatestRates(
                from = "USD",
                to = "BRL,EUR"
            )

            val usdToBrl = response.rates["BRL"]
            val usdToEur = response.rates["EUR"]

            val lista = buildList {
                usdToBrl?.let {
                    add(
                        Cotacao(
                            code = "USD",
                            codeIn = "BRL",
                            nome = "Dólar americano / Real brasileiro",
                            valor = it
                        )
                    )
                }
                usdToEur?.let {
                    add(
                        Cotacao(
                            code = "USD",
                            codeIn = "EUR",
                            nome = "Dólar americano / Euro",
                            valor = it
                        )
                    )
                }
            }

            cachedRates = lista
            lastFetchTimeMillis = now

            Result.success(lista)
        } catch (e: Exception) {
            Log.e("CurrencyRepo", "Erro ao obter cotações Frankfurter", e)
            cachedRates?.let { return Result.success(it) }
            Result.failure(e)
        }
    }
}
