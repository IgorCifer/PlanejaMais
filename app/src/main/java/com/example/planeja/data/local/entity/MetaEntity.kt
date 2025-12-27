package com.example.planeja.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas")
data class MetaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val valorAtual: Double,
    val valorMeta: Double
)
