package com.example.planeja.ui.home

import com.example.planeja.domain.model.Meta
import com.example.planeja.domain.model.Transacao

data class HomeUiState(
    val isLoading: Boolean = true,
    val saldoAtual: Double = 0.0,
    val totalReceitas: Double = 0.0,
    val totalDespesas: Double = 0.0,
    val metas: List<Meta> = emptyList(),
    val transacoesRecentes: List<Transacao> = emptyList(),
    val errorMessage: String? = null
)
