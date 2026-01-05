package com.example.planeja.ui.metas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Meta
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MetasScreen() {
    val app = LocalContext.current.applicationContext as PlanejaApp

    val viewModel: MetasViewModel = viewModel(
        factory = MetasViewModelFactory.create(app)
    )

    val uiState by viewModel.uiState.collectAsState()

    MetasContent(
        uiState = uiState,
        onNovaMeta = { viewModel.iniciarCriacaoMeta() },
        onEditarMeta = { viewModel.iniciarEdicaoMeta(it) },
        onExcluirMeta = { viewModel.deletarMeta(it) },
        onFecharFormulario = { viewModel.fecharFormulario() },
        onSalvarMeta = { viewModel.salvarMeta(it) }
    )
}

@Composable
private fun MetasContent(
    uiState: MetasUiState,
    onNovaMeta: () -> Unit,
    onEditarMeta: (Meta) -> Unit,
    onExcluirMeta: (Meta) -> Unit,
    onFecharFormulario: () -> Unit,
    onSalvarMeta: (Meta) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Metas", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "Erro ao carregar metas",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.metas) { meta ->
                    MetaItem(
                        meta = meta,
                        onEditar = onEditarMeta,
                        onExcluir = onExcluirMeta
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNovaMeta,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nova meta")
        }
    }

    uiState.metaEmEdicao?.let { meta ->
        MetaFormDialog(
            metaInicial = meta,
            onDismiss = onFecharFormulario,
            onConfirm = onSalvarMeta
        )
    }
}

@Composable
private fun MetaItem(
    meta: Meta,
    onEditar: (Meta) -> Unit,
    onExcluir: (Meta) -> Unit
) {
    Card {
        Column(Modifier.padding(12.dp)) {
            Text(meta.titulo, style = MaterialTheme.typography.titleMedium)
            Text("${meta.valorAtual} de ${meta.valorMeta}")
            Text("${(meta.progresso * 100).toInt()}% concluído")

            Row(Modifier.padding(top = 8.dp)) {
                TextButton(onClick = { onEditar(meta) }) { Text("Editar") }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = { onExcluir(meta) }) { Text("Excluir") }
            }
        }
    }
}

@Composable
private fun MetaFormDialog(
    metaInicial: Meta,
    onDismiss: () -> Unit,
    onConfirm: (Meta) -> Unit
) {
    var titulo by remember { mutableStateOf(metaInicial.titulo) }
    var valorAtualText by remember { mutableStateOf(metaInicial.valorAtual.toString()) }
    var valorMetaText by remember { mutableStateOf(metaInicial.valorMeta.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (metaInicial.id == 0L) "Nova meta" else "Editar meta") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título da meta") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = valorAtualText,
                    onValueChange = { valorAtualText = it },
                    label = { Text("Valor atual") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = valorMetaText,
                    onValueChange = { valorMetaText = it },
                    label = { Text("Valor da meta") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val valorAtual = valorAtualText.toDoubleOrNull() ?: 0.0
                val valorMeta = valorMetaText.toDoubleOrNull() ?: 0.0
                onConfirm(
                    metaInicial.copy(
                        titulo = titulo,
                        valorAtual = valorAtual,
                        valorMeta = valorMeta
                    )
                )
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
