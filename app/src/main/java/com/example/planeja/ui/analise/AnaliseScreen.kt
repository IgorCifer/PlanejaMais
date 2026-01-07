package com.example.planeja.ui.analise

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planeja.domain.model.ResumoCategoria

@Composable
fun AnaliseRoute(app: PlanejaApp) {
    val viewModel: AnaliseViewModel = viewModel(
        factory = AnaliseViewModelFactory.create(app)
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    AnaliseScreen(
        state = state,
        onProximoMes = viewModel::irParaProximoMes,
        onMesAnterior = viewModel::irParaMesAnterior
    )
}

@Composable
fun AnaliseScreen(
    state: AnaliseUiState,
    onProximoMes: () -> Unit,
    onMesAnterior: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabeçalho com mês/ano e navegação
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onMesAnterior) {
                Text("<")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${state.mes}/${state.ano}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onProximoMes) {
                Text(">")
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "Total: R$ %.2f".format(state.totalGeral),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(16.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else if (state.resumos.isEmpty()) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = "Nenhuma despesa neste mês",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            // Aqui depois entra a LazyColumn com as barras
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.resumos.forEach { resumo ->
                    Text(
                        text = (resumo.categoria?.nome ?: "Sem categoria") +
                                " - R$ %.2f".format(resumo.total)
                    )
                }
            }
        }
    }
}
