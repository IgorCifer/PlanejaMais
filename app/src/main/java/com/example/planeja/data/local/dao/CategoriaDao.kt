package com.example.planeja.data.local.dao

import androidx.room.*
import com.example.planeja.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Query("SELECT * FROM categorias ORDER BY nome")
    fun getCategorias(): Flow<List<CategoriaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(categoria: CategoriaEntity): Long

    @Update
    suspend fun atualizar(categoria: CategoriaEntity)

    @Delete
    suspend fun deletar(categoria: CategoriaEntity)
}
