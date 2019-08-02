package com.ceaver.bno.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ceaver.bno.Application
import com.ceaver.bno.logging.Log
import com.ceaver.bno.logging.LogDao
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeDao

@androidx.room.Database(entities = [Node::class, Log::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun nodeDao(): NodeDao
    abstract fun logDao(): LogDao

    companion object {
        private var INSTANCE: Database? = null

        fun getInstance(): Database {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = Room.databaseBuilder(Application.appContext!!, Database::class.java, "database").build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}