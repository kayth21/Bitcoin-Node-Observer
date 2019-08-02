package com.ceaver.bno.logging

import android.os.Handler
import android.os.Looper
import com.ceaver.bno.database.Database
import com.ceaver.bno.threading.BackgroundThreadExecutor
import org.greenrobot.eventbus.EventBus
import java.time.LocalDateTime
import java.util.*

object LogRepository {

    fun insertLog(message: String) {
        insertLog(message, UUID.randomUUID())
    }

    fun insertLog(message: String, uuid: UUID) {
        insertLog(Log(0, LocalDateTime.now(), message, uuid))
    }

    fun insertLogAsync(message: String) {
        BackgroundThreadExecutor.execute { insertLog(message, UUID.randomUUID()) }
    }

    fun updateLog(log: Log) {
        getLogDao().updateLog(log)
        EventBus.getDefault().post(LogEvents.Update())
    }

    fun loadLog(identifier: UUID): Log {
        return getLogDao().loadLog(identifier)
    }

    private fun insertLog(log: Log) {
        getLogDao().insertLog(log)
        EventBus.getDefault().post(LogEvents.Insert())
    }

    private fun loadAllLogs(): List<Log> {
        return getLogDao().loadAllLogs()
    }

    fun loadAllLogsAsync(callbackInMainThread: Boolean, callback: (List<Log>) -> Unit) {
        BackgroundThreadExecutor.execute {
            val logs = loadAllLogs()
            if (callbackInMainThread)
                Handler(Looper.getMainLooper()).post { callback.invoke(logs) }
            else
                callback.invoke(logs)
        }
    }

    private fun getLogDao(): LogDao {
        return getDatabase().logDao()
    }

    private fun getDatabase(): Database {
        return Database.getInstance()
    }
}