package com.example.planeja.domain.model

data class ResumoCategoria(
    val categoria: Categoria?,   // null = Sem categoria
    val total: Double,
    val percentual: Double       // 0.0..1.0
)
