package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Meta
import com.example.planeja.domain.repository.MetaRepository

class CriarMetaUseCase(
    private val repository: MetaRepository
) {
    suspend operator fun invoke(meta: Meta) {
        repository.criar(meta)
    }
}
