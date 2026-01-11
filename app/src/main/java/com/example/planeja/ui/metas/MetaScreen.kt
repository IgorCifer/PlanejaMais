package com.example.planeja.ui.metas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Meta
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.SolidColor

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
    val bgLight = MaterialTheme.colorScheme.background
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Metas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF474747)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Defina e acompanhe seus objetivos financeiros",
                    style = MaterialTheme.typography.bodySmall,
                    color = textGray
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage ?: "Erro ao carregar metas",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    uiState.metas.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Surface(
                                    modifier = Modifier.size(64.dp),
                                    shape = MaterialTheme.shapes.extraLarge,
                                    color = Color(0x26479DFF)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Filled.Flag,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Nenhuma meta criada ainda",
                                    color = textGray
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.metas) { meta ->
                                MetaCard(
                                    meta = meta,
                                    onEditar = onEditarMeta,
                                    onExcluir = onExcluirMeta
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onNovaMeta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 16.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        brush = SolidColor(Color(0xFF47F9FF))
                    ),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF47F9FF)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color(0xFF47F9FF)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Nova meta")
                }
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
}

@Composable
private fun MetaCard(
    meta: Meta,
    onEditar: (Meta) -> Unit,
    onExcluir: (Meta) -> Unit
) {
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant
    val textDark = MaterialTheme.colorScheme.onSurface
    val color = MaterialTheme.colorScheme.primary
    val progressoPercent = (meta.progresso * 100.0).coerceIn(0.0, 100.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = color.copy(alpha = 0.12f)
                    ) {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Flag,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = meta.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            color = textDark
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "R$ %.2f de R$ %.2f".format(meta.valorAtual, meta.valorMeta),
                            style = MaterialTheme.typography.bodySmall,
                            color = textGray
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { onEditar(meta) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar meta",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { onExcluir(meta) }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Excluir meta",
                            tint = Color(0xFFD4183D)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = meta.progresso.toFloat().coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(MaterialTheme.shapes.small),
                color = color,
                trackColor = Color(0xFFE5E5E5)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "%.0f%% concluído".format(progressoPercent),
                style = MaterialTheme.typography.bodySmall,
                color = textGray,
                modifier = Modifier.align(Alignment.End)
            )
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
        title = {
            Text(
                text = if (metaInicial.id == 0L) "Nova meta" else "Editar meta",
                fontWeight = FontWeight.Medium
            )
        },
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
