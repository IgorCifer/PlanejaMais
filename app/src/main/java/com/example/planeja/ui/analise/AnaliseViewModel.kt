package com.example.planeja.ui.analise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.usecase.ObterResumoPorCategoriaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class AnaliseViewModel(
    private val obterResumoPorCategoria: ObterResumoPorCategoriaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnaliseUiState())
    val uiState: StateFlow<AnaliseUiState> = _uiState

    init {
        val cal = Calendar.getInstance()
        val mesAtual = cal.get(Calendar.MONTH) + 1
        val anoAtual = cal.get(Calendar.YEAR)

        _uiState.update { it.copy(mes = mesAtual, ano = anoAtual) }
        carregarResumo(mesAtual, anoAtual)
    }

    private fun carregarResumo(mes: Int, ano: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, erro = null) }

            obterResumoPorCategoria(mes, ano).collect { lista ->
                val totalGeral = lista.sumOf { it.total }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mes = mes,
                        ano = ano,
                        totalGeral = totalGeral,
                        resumos = lista,
                        erro = null
                    )
                }
            }
        }
    }

    fun irParaProximoMes() {
        val state = _uiState.value
        var novoMes = state.mes + 1
        var novoAno = state.ano
        if (novoMes > 12) {
            novoMes = 1
            novoAno++
        }
        carregarResumo(novoMes, novoAno)
    }

    fun irParaMesAnterior() {
        val state = _uiState.value
        var novoMes = state.mes - 1
        var novoAno = state.ano
        if (novoMes < 1) {
            novoMes = 12
            novoAno--
        }
        carregarResumo(novoMes, novoAno)
    }
}
