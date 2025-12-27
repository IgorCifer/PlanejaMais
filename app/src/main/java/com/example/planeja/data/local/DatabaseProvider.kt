package com.example.planeja.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: PlanejaDatabase? = null

    fun getDatabase(context: Context): PlanejaDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                PlanejaDatabase::class.java,
                "planeja_db"
            ).build().also { INSTANCE = it }
        }
    }
}