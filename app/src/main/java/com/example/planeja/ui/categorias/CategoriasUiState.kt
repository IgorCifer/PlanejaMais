package com.example.planeja.ui.categorias

import com.example.planeja.domain.model.Categoria

data class CategoriasUiState(
    val isLoading: Boolean = false,
    val categorias: List<Categoria> = emptyList(),
    val nome: String = "",
    val corHex: String = "#FF9800",
    val icone: String = "default",
    val isEditando: Boolean = false,
    val categoriaEmEdicao: Categoria? = null,
    val mostrarDialog: Boolean = false,
    val erro: String? = null
)