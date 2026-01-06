package com.example.planeja.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val corHex: String,    // ex: "#FF9800"
    val icone: String      // ex: "food", "car" (nome lógico por enquanto)
)
