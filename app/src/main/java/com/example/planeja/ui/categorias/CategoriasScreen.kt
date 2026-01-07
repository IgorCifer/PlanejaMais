package com.example.planeja.ui.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Categoria
import androidx.core.graphics.toColorInt

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
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNovoClick) {
                Icon(Icons.Default.Add, contentDescription = "Nova categoria")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                if (state.categorias.isEmpty()) {
                    Text(
                        text = "Nenhuma categoria cadastrada",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            icone = state.icone,
            onNomeChange = onNomeChange,
            onCorChange = onCorChange,
            onIconeChange = onIconeChange,
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(categoria.corHex.toColorInt()), CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = categoria.nome, style = MaterialTheme.typography.bodyLarge)
                Text(text = categoria.icone, style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onEditarClick) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onExcluirClick) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir")
            }
        }
    }
}

@Composable
fun CategoriaDialog(
    isEditando: Boolean,
    nome: String,
    corHex: String,
    icone: String,
    onNomeChange: (String) -> Unit,
    onCorChange: (String) -> Unit,
    onIconeChange: (String) -> Unit,
    onSalvarClick: () -> Unit,
    onDismiss: () -> Unit
) {
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
                    label = { Text("Nome") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = corHex,
                    onValueChange = onCorChange,
                    label = { Text("Cor (hex)") }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = icone,
                    onValueChange = onIconeChange,
                    label = { Text("Ícone (nome lógico)") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSalvarClick) {
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
