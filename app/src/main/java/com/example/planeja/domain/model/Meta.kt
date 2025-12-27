package com.example.planeja.domain.model

data class Meta(
    val id: Long = 0,
    val titulo: String,
    val valorAtual: Double,
    val valorMeta: Double
) {
    val progresso: Double
        get() = if (valorMeta == 0.0) 0.0 else (valorAtual / valorMeta)
}