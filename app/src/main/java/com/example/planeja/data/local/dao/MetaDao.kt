package com.example.planeja.data.local.dao

import androidx.room.*
import com.example.planeja.data.local.entity.MetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDao {

    @Insert
    suspend fun insert(meta: MetaEntity)

    @Update
    suspend fun update(meta: MetaEntity)

    @Delete
    suspend fun delete(meta: MetaEntity)

    @Query("SELECT * FROM metas ORDER BY id DESC")
    fun getMetas(): Flow<List<MetaEntity>>

    @Query("SELECT * FROM metas ORDER BY id DESC LIMIT :limit")
    fun getMetasLimit(limit: Int): Flow<List<MetaEntity>>
}