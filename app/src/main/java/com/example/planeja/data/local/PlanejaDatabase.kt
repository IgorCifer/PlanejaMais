package com.example.planeja.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.planeja.data.local.dao.CategoriaDao
import com.example.planeja.data.local.dao.MetaDao
import com.example.planeja.data.local.dao.TransacaoDao
import com.example.planeja.data.local.dao.UserDao
import com.example.planeja.data.local.entity.CategoriaEntity
import com.example.planeja.data.local.entity.MetaEntity
import com.example.planeja.data.local.entity.TransacaoEntity
import com.example.planeja.data.local.entity.UserEntity

@Database(
    entities = [
        TransacaoEntity::class,
        MetaEntity::class,
        CategoriaEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PlanejaDatabase : RoomDatabase() {
    abstract fun transacaoDao(): TransacaoDao
    abstract fun metaDao(): MetaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun userDao(): UserDao
}