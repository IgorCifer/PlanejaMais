package com.example.planeja.ui.metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.planeja.domain.model.Meta
import com.example.planeja.domain.usecase.AtualizarMetaUseCase
import com.example.planeja.domain.usecase.CriarMetaUseCase
import com.example.planeja.domain.usecase.DeletarMetaUseCase
import com.example.planeja.domain.usecase.ListarMetasHomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.planeja.PlanejaApp


class MetasViewModel(
    private val listarMetasUseCase: ListarMetasHomeUseCase,
    private val criarMetaUseCase: CriarMetaUseCase,
    private val atualizarMetaUseCase: AtualizarMetaUseCase,
    private val deletarMetaUseCase: DeletarMetaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetasUiState(isLoading = true))
    val uiState: StateFlow<MetasUiState> = _uiState.asStateFlow()

    init {
        carregarMetas()
    }

    fun iniciarCriacaoMeta() {
        _uiState.value = _uiState.value.copy(metaEmEdicao = Meta(titulo = "", valorAtual = 0.0, valorMeta = 0.0))
    }

    fun iniciarEdicaoMeta(meta: Meta) {
        _uiState.value = _uiState.value.copy(metaEmEdicao = meta)
    }

    fun fecharFormulario() {
        _uiState.value = _uiState.value.copy(metaEmEdicao = null)
    }

    fun salvarMeta(meta: Meta) {
        viewModelScope.launch {
            if (meta.id == 0L) {
                criarMetaUseCase(meta)
            } else {
                atualizarMetaUseCase(meta)
            }
            fecharFormulario()
        }
    }



    private fun carregarMetas() {
        viewModelScope.launch {
            listarMetasUseCase(Int.MAX_VALUE)
                .catch { e ->
                    _uiState.value = MetasUiState(
                        isLoading = false,
                        metas = emptyList(),
                        errorMessage = e.message
                    )
                }
                .collect { metas ->
                    _uiState.value = MetasUiState(
                        isLoading = false,
                        metas = metas,
                        errorMessage = null
                    )
                }
        }
    }


    fun deletarMeta(meta: Meta) {
        viewModelScope.launch {
            deletarMetaUseCase(meta)
        }
    }
}


object MetasViewModelFactory {
    fun create(app: PlanejaApp) = viewModelFactory {
        initializer {
            MetasViewModel(
                listarMetasUseCase = app.container.listarMetasHomeUseCase,
                criarMetaUseCase = app.container.criarMetaUseCase,
                atualizarMetaUseCase = app.container.atualizarMetaUseCase,
                deletarMetaUseCase = app.container.deletarMetaUseCase
            )
        }
    }
}