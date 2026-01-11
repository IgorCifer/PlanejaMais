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
    val bgLight = MaterialTheme.colorScheme.background
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant
    val textDark = MaterialTheme.colorScheme.onSurface

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgLight)
        ) {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Análise",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF474747)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Entenda seu comportamento financeiro",
                        style = MaterialTheme.typography.bodySmall,
                        color = textGray
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onMesAnterior) {
                            Text("<")
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${mesNumeroParaNomeCurto(state.mes)} ${state.ano}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = textDark
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(onClick = onProximoMes) {
                            Text(">")
                        }
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Total: R$ %.2f".format(state.totalGeral),
                            style = MaterialTheme.typography.bodySmall,
                            color = textDark
                        )
                    }
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.resumos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma despesa neste mês",
                        color = textGray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    items(state.resumos) { resumo ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                ResumoCategoriaBar(
                                    resumo = resumo,
                                    totalGeral = state.totalGeral
                                )
                            }
                        }
                    }
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

