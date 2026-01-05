package com.example.planeja.ui.transacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.usecase.AdicionarTransacaoUseCase
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp

object TransacoesViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            TransacoesViewModel(
                adicionarTransacaoUseCase = app.container.adicionarTransacaoUseCase
            )
        }
    }
}

class TransacoesViewModel(
    private val adicionarTransacaoUseCase: AdicionarTransacaoUseCase
) : ViewModel() {

    fun adicionarTransacao(
        descricao: String,
        valor: Double,
        tipo: TipoTransacao,
        dataMillis: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            val transacao = Transacao(
                descricao = descricao,
                valor = valor,
                tipo = tipo,
                dataMillis = dataMillis,
                categoriaId = null   // depois ligamos com categorias
            )
            adicionarTransacaoUseCase(transacao)
        }
    }
}

