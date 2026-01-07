package com.example.planeja.ui.analise

import com.example.planeja.domain.model.ResumoCategoria

data class AnaliseUiState(
    val isLoading: Boolean = true,
    val mes: Int = 1,              // 1..12
    val ano: Int = 2026,
    val totalGeral: Double = 0.0,
    val resumos: List<ResumoCategoria> = emptyList(),
    val erro: String? = null
)
