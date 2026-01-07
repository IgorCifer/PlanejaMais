package com.example.planeja.ui.transacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Categoria
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.usecase.AdicionarTransacaoUseCase
import com.example.planeja.domain.usecase.ListarCategoriasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NovaTransacaoUiState(
    val descricao: String = "",
    val valorText: String = "",
    val tipo: TipoTransacao = TipoTransacao.DESPESA,
    val categorias: List<Categoria> = emptyList(),
    val categoriaSelecionadaId: Long? = null
)

object TransacoesViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            TransacoesViewModel(
                adicionarTransacaoUseCase = app.container.adicionarTransacaoUseCase,
                listarCategoriasUseCase = app.container.listarCategoriasUseCase
            )
        }
    }
}

class TransacoesViewModel(
    private val adicionarTransacaoUseCase: AdicionarTransacaoUseCase,
    private val listarCategoriasUseCase: ListarCategoriasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NovaTransacaoUiState())
    val uiState: StateFlow<NovaTransacaoUiState> = _uiState

    init {
        carregarCategorias()
    }

    private fun carregarCategorias() {
        viewModelScope.launch {
            listarCategoriasUseCase().collect { lista ->
                _uiState.update { it.copy(categorias = lista) }
            }
        }
    }

    fun onDescricaoChange(novo: String) {
        _uiState.update { it.copy(descricao = novo) }
    }

    fun onValorChange(novo: String) {
        _uiState.update { it.copy(valorText = novo) }
    }

    fun onTipoChange(novo: TipoTransacao) {
        _uiState.update { it.copy(tipo = novo) }
    }

    fun onCategoriaSelecionada(id: Long?) {
        _uiState.update { it.copy(categoriaSelecionadaId = id) }
    }

    fun adicionarTransacao(dataMillis: Long = System.currentTimeMillis(), onSuccess: () -> Unit) {
        val state = _uiState.value
        val valor = state.valorText.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            val transacao = Transacao(
                descricao = state.descricao,
                valor = valor,
                tipo = state.tipo,
                dataMillis = dataMillis,
                categoriaId = state.categoriaSelecionadaId
            )
            adicionarTransacaoUseCase(transacao)
            onSuccess()
        }
    }
}
