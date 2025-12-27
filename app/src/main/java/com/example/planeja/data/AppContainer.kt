package com.example.planeja.data

import android.content.Context
import com.example.planeja.data.local.DatabaseProvider
import com.example.planeja.data.repository.MetaRepositoryImpl
import com.example.planeja.data.repository.TransacaoRepositoryImpl
import com.example.planeja.domain.repository.MetaRepository
import com.example.planeja.domain.repository.TransacaoRepository
import com.example.planeja.domain.usecase.*

class AppContainer(context: Context) {

    // Database + DAOs
    private val database = DatabaseProvider.getDatabase(context)
    private val transacaoDao = database.transacaoDao()
    private val metaDao = database.metaDao()

    // Repositórios
    val transacaoRepository: TransacaoRepository = TransacaoRepositoryImpl(transacaoDao)
    val metaRepository: MetaRepository = MetaRepositoryImpl(metaDao)

    // Use cases de Transações
    val adicionarTransacaoUseCase = AdicionarTransacaoUseCase(transacaoRepository)
    val listarTransacoesRecentesUseCase = ListarTransacoesRecentesUseCase(transacaoRepository)

    // Use cases de Metas
    val criarMetaUseCase = CriarMetaUseCase(metaRepository)
    val atualizarMetaUseCase = AtualizarMetaUseCase(metaRepository)
    val deletarMetaUseCase = DeletarMetaUseCase(metaRepository)
    val listarMetasHomeUseCase = ListarMetasHomeUseCase(metaRepository)
}
