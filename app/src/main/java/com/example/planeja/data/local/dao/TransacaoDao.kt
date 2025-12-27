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
}