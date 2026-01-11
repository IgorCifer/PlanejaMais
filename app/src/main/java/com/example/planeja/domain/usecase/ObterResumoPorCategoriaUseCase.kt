package com.example.planeja.domain.usecase

import com.example.planeja.data.local.dao.CategoriaTotalDto
import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.model.ResumoCategoria
import com.example.planeja.domain.repository.CategoriaRepository
import com.example.planeja.domain.repository.TransacaoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ObterResumoPorCategoriaUseCase(
    private val transacaoRepository: TransacaoRepository,
    private val categoriaRepository: CategoriaRepository
) {

    operator fun invoke(mes: Int, ano: Int): Flow<List<ResumoCategoria>> {
        val mesStr = mes.toString().padStart(2, '0')
        val anoStr = ano.toString()

        val totaisFlow: Flow<List<CategoriaTotalDto>> =
            transacaoRepository.getTotaisDespesasPorCategoria(mesStr, anoStr)

        val categoriasFlow: Flow<List<Categoria>> =
            categoriaRepository.getCategorias()

        return combine(totaisFlow, categoriasFlow) { totais, categorias ->
            val totalGeral = totais.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

            totais.map { dto ->
                val categoria = dto.categoriaId?.let { id ->
                    categorias.firstOrNull { it.id == id }
                }

                ResumoCategoria(
                    categoria = categoria,
                    total = dto.total,
                    percentual = dto.total / totalGeral
                )
            }
        }.map { lista ->
            lista.sortedByDescending { it.total }
        }
    }
}
