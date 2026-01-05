package com.example.planeja.ui.metas

import com.example.planeja.domain.model.Meta

data class MetasUiState(
    val isLoading: Boolean = false,
    val metas: List<Meta> = emptyList(),
    val errorMessage: String? = null,
    val metaEmEdicao: Meta? = null
)

