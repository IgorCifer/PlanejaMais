package com.example.planeja.ui.transacoes


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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planeja.PlanejaApp
import com.example.planeja.domain.model.TipoTransacao


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTransacaoScreen(
    onTransacaoSalva: () -> Unit = {},
    onVoltar: () -> Unit = {}
) {
    val app = LocalContext.current.applicationContext as PlanejaApp
    val viewModel: TransacoesViewModel = viewModel(
        factory = TransacoesViewModelFactory.create(app)
    )

    var descricao by remember { mutableStateOf("") }
    var valorText by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoTransacao.DESPESA) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova transação") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = valorText,
                onValueChange = { valorText = it },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text("Tipo")
            Row {
                RadioButton(
                    selected = tipo == TipoTransacao.DESPESA,
                    onClick = { tipo = TipoTransacao.DESPESA }
                )
                Text("Despesa", modifier = Modifier.padding(end = 16.dp))
                RadioButton(
                    selected = tipo == TipoTransacao.RECEITA,
                    onClick = { tipo = TipoTransacao.RECEITA }
                )
                Text("Receita")
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val valor = valorText.toDoubleOrNull() ?: 0.0
                    viewModel.adicionarTransacao(
                        descricao = descricao,
                        valor = valor,
                        tipo = tipo
                    )
                    onTransacaoSalva()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = descricao.isNotBlank() && valorText.isNotBlank()
            ) {
                Text("Salvar")
            }
        }
    }
}
