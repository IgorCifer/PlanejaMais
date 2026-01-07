package com.example.planeja.data.mapper

import com.example.planeja.data.local.entity.CategoriaEntity
import com.example.planeja.domain.model.Categoria

fun CategoriaEntity.toDomain() = Categoria(
    id = id,
    nome = nome,
    corHex = corHex,
    icone = icone
)

fun Categoria.toEntity() = CategoriaEntity(
    id = id,
    nome = nome,
    corHex = corHex,
    icone = icone
)
