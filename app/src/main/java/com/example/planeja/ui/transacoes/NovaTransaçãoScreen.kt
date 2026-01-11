package com.example.planeja.ui.transacoes

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Label
import androidx.compose.ui.Alignment

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTransacaoScreen(
    transacaoId: Long? = null,
    onTransacaoSalva: () -> Unit = {},
    onVoltar: () -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as PlanejaApp
    val viewModel: TransacoesViewModel = viewModel(
        factory = TransacoesViewModelFactory.create(app)
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var categoriaMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(transacaoId) {
        if (transacaoId != null && viewModel.uiState.value.id == null) {
            viewModel.carregarTransacao(transacaoId)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
                Text(
                    text = "Nova transação",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val isDespesa = state.tipo == TipoTransacao.DESPESA
                val isReceita = state.tipo == TipoTransacao.RECEITA

                FilledTonalButton(
                    onClick = { viewModel.onTipoChange(TipoTransacao.DESPESA) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isDespesa)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isDespesa)
                            MaterialTheme.colorScheme.onError
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Despesa")
                }

                FilledTonalButton(
                    onClick = { viewModel.onTipoChange(TipoTransacao.RECEITA) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isReceita)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isReceita)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Receita")
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = state.valorText,
                onValueChange = viewModel::onValorChange,
                label = { Text("Valor") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Valor"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.descricao,
                onValueChange = viewModel::onDescricaoChange,
                label = { Text("Descrição") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Descrição"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.dataText,
                onValueChange = viewModel::onDataChange,
                label = { Text("Data") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Data"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text(text = "Categoria", style = MaterialTheme.typography.bodyMedium)
            Box {
                OutlinedTextField(
                    value = state.categorias
                        .firstOrNull { it.id == state.categoriaSelecionadaId }
                        ?.nome ?: "Selecione uma categoria",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoriaMenuExpanded = true },
                    enabled = false,
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = "Categoria"
                        )
                    }
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
                    viewModel.salvarTransacao(onSuccess = onTransacaoSalva)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.descricao.isNotBlank() && state.valorText.isNotBlank()
            ) {
                Text("Salvar")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
