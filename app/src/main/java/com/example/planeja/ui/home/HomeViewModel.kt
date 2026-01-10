package com.example.planeja.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.repository.TransacaoRepository
import com.example.planeja.domain.usecase.ListarMetasHomeUseCase
import com.example.planeja.domain.usecase.ListarTransacoesRecentesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.usecase.ObterCotacoesPrincipaisUseCase

object HomeViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            HomeViewModel(
                listarMetasHomeUseCase = app.container.listarMetasHomeUseCase,
                listarTransacoesRecentesUseCase = app.container.listarTransacoesRecentesUseCase,
                transacaoRepository = app.container.transacaoRepository,
                obterCotacoesPrincipaisUseCase = app.container.obterCotacoesPrincipaisUseCase

            )
        }
    }
}

class HomeViewModel(
    private val listarMetasHomeUseCase: ListarMetasHomeUseCase,
    private val listarTransacoesRecentesUseCase: ListarTransacoesRecentesUseCase,
    private val transacaoRepository: TransacaoRepository,
    private val obterCotacoesPrincipaisUseCase: ObterCotacoesPrincipaisUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observarDados()
        carregarCotacoes()
    }

    private fun observarDados() {
        viewModelScope.launch {
            // fluxo de metas (limite 3)
            val metasFlow = listarMetasHomeUseCase(3)

            // fluxo de transações recentes (limite 5)
            val transacoesRecentesFlow = listarTransacoesRecentesUseCase(5)

            // todas as transações para calcular saldo
            val todasTransacoesFlow = transacaoRepository.getTodasTransacoes()

            combine(
                metasFlow,
                transacoesRecentesFlow,
                todasTransacoesFlow
            ) { metas, transacoesRecentes, todasTransacoes ->
                val totalReceitas = todasTransacoes
                    .filter { it.tipo == TipoTransacao.RECEITA }
                    .sumOf { it.valor }
                val totalDespesas = todasTransacoes
                    .filter { it.tipo == TipoTransacao.DESPESA }
                    .sumOf { it.valor }
                val saldo = totalReceitas - totalDespesas

                HomeUiState(
                    isLoading = false,
                    saldoAtual = saldo,
                    totalReceitas = totalReceitas,
                    totalDespesas = totalDespesas,
                    metas = metas,
                    transacoesRecentes = transacoesRecentes,
                    errorMessage = null
                )
            }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun carregarCotacoes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingCotacoes = true,
                erroCotacoes = null
            )

            val resultado = obterCotacoesPrincipaisUseCase()

            _uiState.value = resultado.fold(
                onSuccess = { lista ->
                    _uiState.value.copy(
                        isLoadingCotacoes = false,
                        cotacoes = lista,
                        erroCotacoes = null
                    )
                },
                onFailure = { e ->
                    _uiState.value.copy(
                        isLoadingCotacoes = false,
                        cotacoes = emptyList(),
                        erroCotacoes = e.message ?: "Erro ao carregar cotações"
                    )
                }
            )
        }
    }


}