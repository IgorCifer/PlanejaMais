package com.example.planeja.ui.transacoes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.model.Transacao
import com.example.planeja.domain.usecase.AdicionarTransacaoUseCase
import com.example.planeja.domain.usecase.AtualizarTransacaoUseCase
import com.example.planeja.domain.usecase.ListarCategoriasUseCase
import com.example.planeja.domain.usecase.ObterTransacaoPorIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TransacoesViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            TransacoesViewModel(
                adicionarTransacaoUseCase = app.container.adicionarTransacaoUseCase,
                atualizarTransacaoUseCase = app.container.atualizarTransacaoUseCase,
                obterTransacaoPorIdUseCase = app.container.obterTransacaoPorIdUseCase,
                listarCategoriasUseCase = app.container.listarCategoriasUseCase
            )
        }
    }
}

class TransacoesViewModel(
    private val adicionarTransacaoUseCase: AdicionarTransacaoUseCase,
    private val atualizarTransacaoUseCase: AtualizarTransacaoUseCase,
    private val obterTransacaoPorIdUseCase: ObterTransacaoPorIdUseCase,
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

    fun onDataChange(novaData: String) {
        _uiState.update { it.copy(dataText = novaData) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun carregarTransacao(id: Long) {
        viewModelScope.launch {
            obterTransacaoPorIdUseCase(id).collect { transacao ->
                if (transacao != null) {
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val instant = java.time.Instant.ofEpochMilli(transacao.dataMillis)
                    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    val dataText = formatter.format(localDate)

                    _uiState.update {
                        it.copy(
                            id = transacao.id,
                            descricao = transacao.descricao,
                            valorText = transacao.valor.toString(),
                            tipo = transacao.tipo,
                            categoriaSelecionadaId = transacao.categoriaId,
                            dataText = dataText
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun salvarTransacao(onSuccess: () -> Unit) {
        val state = _uiState.value
        val valor = state.valorText.toDoubleOrNull() ?: 0.0

        val dataMillis = if (state.dataText.isBlank()) {
            System.currentTimeMillis()
        } else {
            try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDate = LocalDate.parse(state.dataText, formatter)
                localDate
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }
        }

        viewModelScope.launch {
            val transacao = Transacao(
                id = state.id ?: 0L,
                descricao = state.descricao,
                valor = valor,
                tipo = state.tipo,
                dataMillis = dataMillis,
                categoriaId = state.categoriaSelecionadaId
            )

            if (state.id == null) {
                adicionarTransacaoUseCase(transacao)
            } else {
                atualizarTransacaoUseCase(transacao)
            }
            onSuccess()
        }
    }
}
