package com.example.planeja.domain.model

data class Transacao(
    val id: Long = 0,
    val tipo: TipoTransacao,
    val valor: Double,
    val descricao: String,
    val dataMillis: Long,
    val categoriaId: Long?
)

enum class TipoTransacao {
    RECEITA,
    DESPESA
}
