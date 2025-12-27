package com.example.planeja.data.mapper

import com.example.planeja.data.local.entity.MetaEntity
import com.example.planeja.domain.model.Meta

fun MetaEntity.toDomain(): Meta =
    Meta(
        id = id,
        titulo = titulo,
        valorAtual = valorAtual,
        valorMeta = valorMeta
    )

fun Meta.toEntity(): MetaEntity =
    MetaEntity(
        id = id,
        titulo = titulo,
        valorAtual = valorAtual,
        valorMeta = valorMeta
    )
