package com.example.planeja.ui.analise

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.planeja.domain.model.ResumoCategoria
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.lazy.items

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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onMesAnterior) {
                Text("<")
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = "${mesNumeroParaNomeCurto(state.mes)} ${state.ano}",
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(state.resumos) { resumo ->
                    ResumoCategoriaBar(
                        resumo = resumo,
                        totalGeral = state.totalGeral
                    )
                }
            }
        }
    }
}

@Composable
fun ResumoCategoriaBar(
    resumo: ResumoCategoria,
    totalGeral: Double
) {
    val nome = resumo.categoria?.nome ?: "Sem categoria"
    val corHex = resumo.categoria?.corHex ?: "#2196F3"
    val corBarra = try {
        Color(corHex.toColorInt())
    } catch (e: IllegalArgumentException) {
        Color(0xFF2196F3)
    }

    val percentual = if (totalGeral > 0) {
        (resumo.total / totalGeral).toFloat()
    } else 0f

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = nome,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "R$ %.2f (%.0f%%)".format(resumo.total, percentual * 100),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentual.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(
                        color = corBarra,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

private fun mesNumeroParaNomeCurto(mes: Int): String =
    when (mes) {
        1 -> "Jan"
        2 -> "Fev"
        3 -> "Mar"
        4 -> "Abr"
        5 -> "Mai"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Ago"
        9 -> "Set"
        10 -> "Out"
        11 -> "Nov"
        12 -> "Dez"
        else -> mes.toString()
    }

