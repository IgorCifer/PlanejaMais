package com.example.planeja.domain.model

data class Cotacao(
    val code: String,       // ex: "USD"
    val codeIn: String,     // ex: "BRL"
    val nome: String,       // ex: "Dólar Americano/Real Brasileiro"
    val valor: Double       // bid parseado
)
