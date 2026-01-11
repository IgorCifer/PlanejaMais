package com.example.planeja.data

import android.content.Context
import com.example.planeja.data.local.DatabaseProvider
import com.example.planeja.data.repository.CategoriaRepositoryImpl
import com.example.planeja.data.repository.AuthRepositoryImpl
import com.example.planeja.data.repository.MetaRepositoryImpl
import com.example.planeja.data.repository.TransacaoRepositoryImpl
import com.example.planeja.domain.repository.MetaRepository
import com.example.planeja.domain.repository.TransacaoRepository
import com.example.planeja.domain.usecase.*
import com.example.planeja.data.remote.RemoteModule
import com.example.planeja.data.repository.CurrencyRepositoryImpl
import com.example.planeja.domain.repository.CurrencyRepository
import com.example.planeja.domain.usecase.ObterCotacoesPrincipaisUseCase
import com.example.planeja.data.repository.NotificationRepository
import com.example.planeja.domain.repository.AuthRepository


class AppContainer(private val context: Context) {

    private val database = DatabaseProvider.getDatabase(context)
    private val transacaoDao = database.transacaoDao()
    private val metaDao = database.metaDao()
    private val categoriaDao = database.categoriaDao()
    private val userDao = database.userDao()

    val transacaoRepository: TransacaoRepository = TransacaoRepositoryImpl(transacaoDao)
    val metaRepository: MetaRepository = MetaRepositoryImpl(metaDao)
    val categoriaRepository = CategoriaRepositoryImpl(categoriaDao)

    val authRepository: AuthRepository = AuthRepositoryImpl(userDao, context)

    val adicionarTransacaoUseCase = AdicionarTransacaoUseCase(transacaoRepository)
    val listarTransacoesRecentesUseCase = ListarTransacoesRecentesUseCase(transacaoRepository)
    val atualizarTransacaoUseCase = AtualizarTransacaoUseCase(transacaoRepository)

    val deletarTransacaoUseCase = DeletarTransacaoUseCase(transacaoRepository)
    val obterTransacaoPorIdUseCase = ObterTransacaoPorIdUseCase(transacaoRepository)


    val criarMetaUseCase = CriarMetaUseCase(metaRepository)
    val atualizarMetaUseCase = AtualizarMetaUseCase(metaRepository)
    val deletarMetaUseCase = DeletarMetaUseCase(metaRepository)
    val listarMetasHomeUseCase = ListarMetasHomeUseCase(metaRepository)

    val criarCategoriaUseCase = CriarCategoriaUseCase(categoriaRepository)
    val atualizarCategoriaUseCase = AtualizarCategoriaUseCase(categoriaRepository)
    val deletarCategoriaUseCase = DeletarCategoriaUseCase(categoriaRepository)
    val listarCategoriasUseCase = ListarCategoriasUseCase(categoriaRepository)
    val obterResumoPorCategoriaUseCase = ObterResumoPorCategoriaUseCase(
        transacaoRepository = transacaoRepository,
        categoriaRepository = categoriaRepository
    )

    val currencyRepository: CurrencyRepository = CurrencyRepositoryImpl(
        api = RemoteModule.frankfurterApi
    )
    val obterCotacoesPrincipaisUseCase = ObterCotacoesPrincipaisUseCase(currencyRepository)
    val notificationRepository: NotificationRepository by lazy {
        NotificationRepository(context)
    }
}
