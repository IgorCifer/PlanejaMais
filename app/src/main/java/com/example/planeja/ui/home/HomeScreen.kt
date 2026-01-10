package com.example.planeja.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.Cotacao
import com.example.planeja.domain.model.Transacao

@Composable
fun HomeScreen(
    onVerTodasMetas: () -> Unit = {},
    onNovaTransacao: () -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as PlanejaApp
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory.create(app))
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNovaTransacao) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nova transação"
                )
            }
        }
    ) { padding ->
        HomeContent(
            uiState = uiState,
            onVerTodasMetas = onVerTodasMetas,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onVerTodasMetas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Seu saldo atual", style = MaterialTheme.typography.labelMedium)
        Text(
            text = "R$ %.2f".format(uiState.saldoAtual),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text("Receitas")
                Text("R$ %.2f".format(uiState.totalReceitas))
            }
            Column(Modifier.weight(1f)) {
                Text("Despesas")
                Text("R$ %.2f".format(uiState.totalDespesas))
            }
        }

        Spacer(Modifier.height(24.dp))

        // Card de cotações
        when {
            uiState.isLoadingCotacoes -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
            }
            uiState.cotacoes.isNotEmpty() -> {
                CotacoesCard(uiState.cotacoes)
                Spacer(Modifier.height(24.dp))
            }
            uiState.erroCotacoes != null -> {
                Text(
                    text = "Erro ao carregar cotações",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(16.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Suas metas", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = onVerTodasMetas) { Text("Ver todas") }
        }

        uiState.metas.take(3).forEach { meta ->
            Text("${meta.titulo} - ${"%.0f".format(meta.progresso * 100)}%")
        }

        Spacer(Modifier.height(24.dp))

        Text("Transações recentes", style = MaterialTheme.typography.titleMedium)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.transacoesRecentes) { transacao ->
                TransacaoItemHome(transacao)
            }
        }
    }
}

@Composable
private fun TransacaoItemHome(transacao: Transacao) {
    Text("${transacao.descricao} - R$ %.2f".format(transacao.valor))
}

@Composable
private fun CotacoesCard(cotacoes: List<Cotacao>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Cotações hoje",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            cotacoes.forEach { c ->
                Text(
                    text = "${c.code}/${c.codeIn}: R$ %.4f".format(c.valor),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
