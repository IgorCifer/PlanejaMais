package com.example.planeja.ui.transacoes

import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.model.TipoTransacao

data class NovaTransacaoUiState(
    val id: Long? = null,
    val descricao: String = "",
    val valorText: String = "",
    val tipo: TipoTransacao = TipoTransacao.DESPESA,
    val categorias: List<Categoria> = emptyList(),
    val categoriaSelecionadaId: Long? = null,
    val dataText: String = ""
)
