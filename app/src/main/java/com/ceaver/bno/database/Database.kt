package com.ceaver.bno.database

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.ceaver.bno.Application
import com.ceaver.bno.nodes.Node
import com.ceaver.bno.nodes.NodeDao

@android.arch.persistence.room.Database(entities = [Node::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun nodeDao(): NodeDao

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