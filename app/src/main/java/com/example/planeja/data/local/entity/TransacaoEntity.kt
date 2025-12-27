package com.example.planeja.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class TransacaoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipo: String,          // "RECEITA" ou "DESPESA"
    val valor: Double,
    val descricao: String,
    val data: Long,            // timestamp em millis
    val categoriaId: Long?
)
