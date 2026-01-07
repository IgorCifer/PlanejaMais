package com.example.planeja.ui.transacoes

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.TipoTransacao


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTransacaoScreen(
    onTransacaoSalva: () -> Unit = {},
    onVoltar: () -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as PlanejaApp
    val viewModel: TransacoesViewModel = viewModel(
        factory = TransacoesViewModelFactory.create(app)
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var categoriaMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova transação") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.descricao,
                onValueChange = viewModel::onDescricaoChange,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = state.valorText,
                onValueChange = viewModel::onValorChange,
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text("Tipo")
            Row {
                RadioButton(
                    selected = state.tipo == TipoTransacao.DESPESA,
                    onClick = { viewModel.onTipoChange(TipoTransacao.DESPESA) }
                )
                Text("Despesa", modifier = Modifier.padding(end = 16.dp))
                RadioButton(
                    selected = state.tipo == TipoTransacao.RECEITA,
                    onClick = { viewModel.onTipoChange(TipoTransacao.RECEITA) }
                )
                Text("Receita")
            }

            Spacer(Modifier.height(8.dp))

            // Seleção de categoria
            Text("Categoria")
            Box {
                OutlinedTextField(
                    value = state.categorias
                        .firstOrNull { it.id == state.categoriaSelecionadaId }
                        ?.nome ?: "Nenhuma",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoriaMenuExpanded = true },
                    enabled = false,
                    readOnly = true
                )

                DropdownMenu(
                    expanded = categoriaMenuExpanded,
                    onDismissRequest = { categoriaMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Nenhuma") },
                        onClick = {
                            viewModel.onCategoriaSelecionada(null)
                            categoriaMenuExpanded = false
                        }
                    )
                    state.categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nome) },
                            onClick = {
                                viewModel.onCategoriaSelecionada(categoria.id)
                                categoriaMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.adicionarTransacao(onSuccess = onTransacaoSalva)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.descricao.isNotBlank() && state.valorText.isNotBlank()
            ) {
                Text("Salvar")
            }
        }
    }
}
