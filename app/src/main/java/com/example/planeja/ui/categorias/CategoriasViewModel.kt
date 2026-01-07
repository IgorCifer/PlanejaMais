package com.example.planeja.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.usecase.AtualizarCategoriaUseCase
import com.example.planeja.domain.usecase.CriarCategoriaUseCase
import com.example.planeja.domain.usecase.DeletarCategoriaUseCase
import com.example.planeja.domain.usecase.ListarCategoriasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val listarCategorias: ListarCategoriasUseCase,
    private val criarCategoria: CriarCategoriaUseCase,
    private val atualizarCategoria: AtualizarCategoriaUseCase,
    private val deletarCategoria: DeletarCategoriaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriasUiState(isLoading = true))
    val uiState: StateFlow<CategoriasUiState> = _uiState

    init {
        observarCategorias()
    }

    private fun observarCategorias() {
        viewModelScope.launch {
            listarCategorias().collect { lista ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categorias = lista,
                        erro = null
                    )
                }
            }
        }
    }

    fun onNomeChange(novo: String) {
        _uiState.update { it.copy(nome = novo) }
    }

    fun onCorChange(nova: String) {
        _uiState.update { it.copy(corHex = nova) }
    }

    fun onIconeChange(novo: String) {
        _uiState.update { it.copy(icone = novo) }
    }

    fun abrirDialogCriar() {
        _uiState.update {
            it.copy(
                mostrarDialog = true,
                isEditando = false,
                categoriaEmEdicao = null,
                nome = "",
                corHex = "#FF9800",
                icone = "default",
                erro = null
            )
        }
    }

    fun abrirDialogEditar(categoria: Categoria) {
        _uiState.update {
            it.copy(
                mostrarDialog = true,
                isEditando = true,
                categoriaEmEdicao = categoria,
                nome = categoria.nome,
                corHex = categoria.corHex,
                icone = categoria.icone,
                erro = null
            )
        }
    }

    fun fecharDialog() {
        _uiState.update { it.copy(mostrarDialog = false, erro = null) }
    }

    fun salvarCategoria() {
        val state = _uiState.value
        if (state.nome.isBlank()) {
            _uiState.update { it.copy(erro = "Nome obrigatório") }
            return
        }

        viewModelScope.launch {
            try {
                if (state.isEditando && state.categoriaEmEdicao != null) {
                    val atualizada = state.categoriaEmEdicao.copy(
                        nome = state.nome,
                        corHex = state.corHex,
                        icone = state.icone
                    )
                    atualizarCategoria(atualizada)
                } else {
                    val nova = Categoria(
                        nome = state.nome,
                        corHex = state.corHex,
                        icone = state.icone
                    )
                    criarCategoria(nova)
                }
                fecharDialog()
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao salvar") }
            }
        }
    }

    fun deletar(categoria: Categoria) {
        viewModelScope.launch {
            try {
                deletarCategoria(categoria)
            } catch (e: Exception) {
                _uiState.update { it.copy(erro = "Erro ao excluir") }
            }
        }
    }
}
