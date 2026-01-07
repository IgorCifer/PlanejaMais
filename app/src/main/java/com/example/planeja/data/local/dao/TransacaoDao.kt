package com.example.planeja.data.local.dao

import androidx.room.*
import com.example.planeja.data.local.entity.TransacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {

    @Insert
    suspend fun insert(transacao: TransacaoEntity)

    @Update
    suspend fun update(transacao: TransacaoEntity)

    @Delete
    suspend fun delete(transacao: TransacaoEntity)

    @Query("SELECT * FROM transacoes ORDER BY data DESC")
    fun getAll(): Flow<List<TransacaoEntity>>

    @Query("SELECT * FROM transacoes ORDER BY data DESC LIMIT :limit")
    fun getRecentes(limit: Int): Flow<List<TransacaoEntity>>

    @Query(
        """
        SELECT 
            categoriaId AS categoriaId,
            SUM(valor) AS total
        FROM transacoes
        WHERE tipo = 'DESPESA'
          AND strftime('%m', datetime(data / 1000, 'unixepoch')) = :mesStr
          AND strftime('%Y', datetime(data / 1000, 'unixepoch')) = :anoStr
        GROUP BY categoriaId
        """
    )
    fun getTotaisDespesasPorCategoria(
        mesStr: String,  // ex: "01", "02"... "12"
        anoStr: String   // ex: "2026"
    ): Flow<List<CategoriaTotalDto>>
}