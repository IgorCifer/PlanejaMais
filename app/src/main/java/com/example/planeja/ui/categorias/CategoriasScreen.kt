package com.example.planeja.ui.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Categoria
import androidx.core.graphics.toColorInt
import androidx.compose.ui.unit.sp


@Composable
fun CategoriasRoute(app: PlanejaApp) {
    val viewModel: CategoriasViewModel = viewModel(
        factory = CategoriasViewModelFactory.create(app)
    )

    val state by viewModel.uiState.collectAsState()

    CategoriasScreen(
        state = state,
        onNovoClick = viewModel::abrirDialogCriar,
        onEditarClick = viewModel::abrirDialogEditar,
        onExcluirClick = viewModel::deletar,
        onNomeChange = viewModel::onNomeChange,
        onSalvarClick = viewModel::salvarCategoria,
        onFecharDialog = viewModel::fecharDialog,
        onCorChange = viewModel::onCorChange,
        onIconeChange = viewModel::onIconeChange
    )
}

@Composable
fun CategoriasScreen(
    state: CategoriasUiState,
    onNovoClick: () -> Unit,
    onEditarClick: (Categoria) -> Unit,
    onExcluirClick: (Categoria) -> Unit,
    onNomeChange: (String) -> Unit,
    onCorChange: (String) -> Unit,
    onIconeChange: (String) -> Unit,
    onSalvarClick: () -> Unit,
    onFecharDialog: () -> Unit
) {
    val bgLight = MaterialTheme.colorScheme.background
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovoClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(y = (-8).dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova categoria")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgLight)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Categorias",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF474747)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Organize seus gastos por categoria",
                        style = MaterialTheme.typography.bodySmall,
                        color = textGray
                    )
                }

                Spacer(Modifier.height(16.dp))

                when {
                    state.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    state.categorias.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhuma categoria cadastrada",
                                color = textGray
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.categorias) { categoria ->
                                CategoriaItem(
                                    categoria = categoria,
                                    onEditarClick = { onEditarClick(categoria) },
                                    onExcluirClick = { onExcluirClick(categoria) }
                                )
                            }
                        }
                    }
                }
            }

            if (state.erro != null) {
                Text(
                    text = state.erro,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }

    if (state.mostrarDialog) {
        CategoriaDialog(
            isEditando = state.isEditando,
            nome = state.nome,
            corHex = state.corHex,
            onNomeChange = onNomeChange,
            onCorChange = onCorChange,
            onSalvarClick = onSalvarClick,
            onDismiss = onFecharDialog
        )
    }
}

@Composable
fun CategoriaItem(
    categoria: Categoria,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    val textDark = MaterialTheme.colorScheme.onSurface
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant

    val corIcone = try {
        Color(categoria.corHex.toColorInt())
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(corIcone.copy(alpha = 0.12f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(corIcone, shape = CircleShape)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = categoria.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textDark
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Gasto total",
                    style = MaterialTheme.typography.bodySmall,
                    color = textGray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditarClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar categoria",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onExcluirClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir categoria",
                        tint = Color(0xFFD4183D)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriaDialog(
    isEditando: Boolean,
    nome: String,
    corHex: String,
    onNomeChange: (String) -> Unit,
    onCorChange: (String) -> Unit,
    onSalvarClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val cores = listOf(
        "#FF6B6B",
        "#4ECDC4",
        "#FFD93D",
        "#1A759F",
        "#9D4EDD"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditando) "Editar categoria" else "Nova categoria")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = onNomeChange,
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text(text = "Cor", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    cores.forEach { hex ->
                        val cor = try {
                            Color(hex.toColorInt())
                        } catch (e: Exception) {
                            Color.Gray
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(cor, CircleShape)
                                .border(
                                    width = if (corHex == hex) 2.dp else 0.dp,
                                    color = if (corHex == hex) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { onCorChange(hex) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSalvarClick) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
