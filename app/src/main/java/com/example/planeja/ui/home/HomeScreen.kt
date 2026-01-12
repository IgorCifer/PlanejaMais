package com.example.planeja.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.planeja.domain.model.Cotacao
import com.example.planeja.domain.model.TipoTransacao
import com.example.planeja.domain.model.Transacao
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.remember

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    userName: String,
    onVerTodasMetas: () -> Unit = {},
    onNovaTransacao: () -> Unit = {},
    onEditarTransacao: (Long) -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as PlanejaApp
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory.create(app))
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNovaTransacao,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(y = (-16).dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nova transação"
                )
            }
        }
    ) { innerPadding ->
        HomeContent(
            uiState = uiState,
            userName = userName,
            onVerTodasMetas = onVerTodasMetas,
            innerPadding = innerPadding,
            onEditarTransacao = onEditarTransacao,
            onExcluirTransacao = { t -> viewModel.deletarTransacao(t) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    userName: String,
    onVerTodasMetas: () -> Unit,
    innerPadding: PaddingValues,
    onEditarTransacao: (Long) -> Unit,
    onExcluirTransacao: (Transacao) -> Unit
) {

    val headerColor = MaterialTheme.colorScheme.primary
    val incomeColor = Color(0xFF47F9FF)
    val expenseColor = Color(0xFF4D47FF)
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant
    val textDark = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )  {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp, vertical = 17.dp)
        ) {
            Column {
                Text(
                    text = "Olá, $userName",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Seu saldo atual",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "R$ %.2f".format(uiState.saldoAtual),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResumoCard(
                        titulo = "Receitas",
                        valor = uiState.totalReceitas,
                        badgeColor = incomeColor.copy(alpha = 0.15f),
                        iconColor = incomeColor,
                        textGray = textGray,
                        textDark = textDark,
                        modifier = Modifier.weight(1f)
                    )
                    ResumoCard(
                        titulo = "Despesas",
                        valor = uiState.totalDespesas,
                        badgeColor = expenseColor.copy(alpha = 0.15f),
                        iconColor = expenseColor,
                        textGray = textGray,
                        textDark = textDark,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                when {
                    uiState.isLoadingCotacoes -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Carregando cotações...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textGray
                                )
                            }
                        }
                    }

                    uiState.cotacoes.isNotEmpty() -> {
                        CotacoesCardHome(
                            cotacoes = uiState.cotacoes,
                            textDark = textDark,
                            textGray = textGray
                        )
                    }

                    uiState.erroCotacoes != null -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = "Erro ao carregar cotações",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = uiState.erroCotacoes ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Suas metas",
                        style = MaterialTheme.typography.titleMedium,
                        color = textDark
                    )
                    TextButton(onClick = onVerTodasMetas) {
                        Text(
                            text = "Ver todas",
                            style = MaterialTheme.typography.bodySmall,
                            color = headerColor
                        )
                    }
                }
            }

            items(uiState.metas.take(3)) { meta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = meta.titulo,
                            style = MaterialTheme.typography.titleSmall,
                            color = textDark
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "R$ %.2f de R$ %.2f".format(
                                meta.valorAtual,
                                meta.valorMeta
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = textGray
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { meta.progresso.toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(MaterialTheme.shapes.small),
                            color = headerColor,
                            trackColor = Color(0xFFE5E5E5)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "%.0f%% concluído".format(meta.progresso * 100),
                            style = MaterialTheme.typography.bodySmall,
                            color = textGray,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Transações recentes",
                    style = MaterialTheme.typography.titleMedium,
                    color = textDark
                )
            }

            items(uiState.transacoesRecentes, key = { it.id }) { transacao ->
                TransacaoItemHomeSwipe(
                    transacao = transacao,
                    onEditar = { t -> onEditarTransacao(t.id) },
                    onExcluir = { t -> onExcluirTransacao(t) }
                )
            }
        }
    }
}

@Composable
private fun ResumoCard(
    titulo: String,
    valor: Double,
    badgeColor: Color,
    iconColor: Color,
    textGray: Color,
    textDark: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 96.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium,
                color = textGray
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "R$ %.2f".format(valor),
                style = MaterialTheme.typography.titleLarge,
                color = textDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
private fun CotacoesCardHome(
    cotacoes: List<Cotacao>,
    textDark: Color,
    textGray: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Cotações de moedas",
                style = MaterialTheme.typography.titleMedium,
                color = textDark
            )
            Spacer(Modifier.height(8.dp))

            cotacoes.forEachIndexed { index, c ->
                val hasDivider = index < cotacoes.lastIndex
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${c.code} (${c.codeIn})",
                        color = textGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "R$ %.4f".format(c.valor),
                        color = textDark,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (hasDivider) {
                    HorizontalDivider(color = Color(0xFFEDEDED))
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Dados fornecidos pela Frankfurter API",
                style = MaterialTheme.typography.bodySmall,
                color = textGray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TransacaoItemHome(
    transacao: Transacao,
    onClick: () -> Unit
) {
    val isReceita = transacao.tipo == TipoTransacao.RECEITA
    val valueColor = if (isReceita) Color(0xFF47F9FF) else Color(0xFF4D47FF)

    val dataStr = remember(transacao.dataMillis) {
        val instant = java.time.Instant.ofEpochMilli(transacao.dataMillis)
        val localDate = java.time.LocalDateTime.ofInstant(
            instant,
            java.time.ZoneId.systemDefault()
        ).toLocalDate()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        formatter.format(localDate)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transacao.descricao,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = dataStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = (if (isReceita) "+R$ " else "-R$ ") +
                        "%.2f".format(kotlin.math.abs(transacao.valor)),
                style = MaterialTheme.typography.bodyLarge,
                color = valueColor
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TransacaoItemHomeSwipe(
    transacao: Transacao,
    onEditar: (Transacao) -> Unit,
    onExcluir: (Transacao) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = { totalDistance: Float ->
            totalDistance * 0.5f
        }
    )

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd,
            SwipeToDismissBoxValue.EndToStart -> {
                onExcluir(transacao)
            }
            SwipeToDismissBoxValue.Settled -> Unit
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when (direction) {
                            SwipeToDismissBoxValue.StartToEnd,
                            SwipeToDismissBoxValue.EndToStart -> Color(0xFFF44336)
                            else -> Color.Transparent
                        }
                    )
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Arrangement.Start
                    SwipeToDismissBoxValue.EndToStart -> Arrangement.End
                    else -> Arrangement.Center
                }
            ) {
                when (direction) {
                    SwipeToDismissBoxValue.StartToEnd,
                    SwipeToDismissBoxValue.EndToStart -> Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Color.White
                    )
                    else -> Unit
                }
            }
        }
    ) {
        TransacaoItemHome(
            transacao = transacao,
            onClick = { onEditar(transacao) }
        )
    }
}


