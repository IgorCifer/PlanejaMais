package com.example.planeja.domain.usecase

import com.example.planeja.domain.model.Meta
import com.example.planeja.domain.repository.MetaRepository
import kotlinx.coroutines.flow.Flow

class ListarMetasHomeUseCase(
    private val repository: MetaRepository
) {
    operator fun invoke(limit: Int): Flow<List<Meta>> {
        return repository.getMetasHome(limit)
    }
}
