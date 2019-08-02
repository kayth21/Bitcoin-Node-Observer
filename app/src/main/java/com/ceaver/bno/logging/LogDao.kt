package com.ceaver.bno.logging

import androidx.room.*
import java.util.*

@Dao
interface LogDao {
    @Query("select * from log")
    fun loadAllLogs(): List<Log>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertLog(log: Log)

    @Update
    fun updateLog(log: Log)

    @Delete
    fun deleteLog(log: Log)

    @Query("delete from log")
    fun deleteAllLog()

    @Query("select * from log where id = :id")
    fun loadLog(id: Long): Log

    @Query("select * from log where uuid = :uuid")
    fun loadLog(uuid: UUID): Log
}