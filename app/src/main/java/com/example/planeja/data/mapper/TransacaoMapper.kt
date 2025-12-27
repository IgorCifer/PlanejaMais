package com.example.planeja.data.mapper

import com.example.planeja.data.local.entity.TransacaoEntity
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.model.Transacao

fun TransacaoEntity.toDomain(): Transacao =
    Transacao(
        id = id,
        tipo = if (tipo == "RECEITA") TipoTransacao.RECEITA else TipoTransacao.DESPESA,
        valor = valor,
        descricao = descricao,
        dataMillis = data,
        categoriaId = categoriaId
    )

fun Transacao.toEntity(): TransacaoEntity =
    TransacaoEntity(
        id = id,
        tipo = if (tipo == TipoTransacao.RECEITA) "RECEITA" else "DESPESA",
        valor = valor,
        descricao = descricao,
        data = dataMillis,
        categoriaId = categoriaId
    )
