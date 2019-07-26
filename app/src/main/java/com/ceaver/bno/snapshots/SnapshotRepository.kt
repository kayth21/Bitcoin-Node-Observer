package com.ceaver.bno.snapshots

import android.content.Context
import com.ceaver.bno.Application
import com.ceaver.bno.extensions.getInt
import com.ceaver.bno.extensions.getLong
import com.ceaver.bno.extensions.getString
import com.ceaver.bno.network.SyncStatus
import org.greenrobot.eventbus.EventBus
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val SHARED_PREFERENCES_KEY = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences"
private const val LATEST_BLOCK = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.latestBlock"
private const val TOTAL_NODES = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.totalNodes"
private const val SNAPSHOT_DATE = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.snapshotDate"
private const val LAST_SYNC_STATUS = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.lastSyncStatus"
private const val LAST_SYNC_DATE = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.lastSyncDate"
private const val ERROR_TEXT = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.errorText"

object SnapshotRepository {

    fun update(snapshot: Snapshot) {
        Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit()
            .putInt(LATEST_BLOCK, snapshot.blockHeight ?: Int.MIN_VALUE)
            .putInt(TOTAL_NODES, snapshot.totalNodes ?: Int.MIN_VALUE)
            .putLong(SNAPSHOT_DATE, if (snapshot.snapshotDate == null) Long.MIN_VALUE else ChronoUnit.SECONDS.between(LocalDateTime.MIN, snapshot.snapshotDate))
            .putString(LAST_SYNC_STATUS, snapshot.lastSyncStatus?.name)
            .putLong(LAST_SYNC_DATE, if (snapshot.lastSyncDate == null) Long.MIN_VALUE else ChronoUnit.SECONDS.between(LocalDateTime.MIN, snapshot.lastSyncDate))
            .putString(ERROR_TEXT, snapshot.errorMessage)
            .apply()
        EventBus.getDefault().post(SnapshotEvents.Update())
    }

    fun load(): Snapshot {
        val preferences = Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return Snapshot(
            preferences.getInt(LATEST_BLOCK),
            preferences.getInt(TOTAL_NODES),
            preferences.getLong(SNAPSHOT_DATE)?.let { LocalDateTime.MIN.plusSeconds(it) },
            preferences.getString(LAST_SYNC_STATUS)?.let { SyncStatus.valueOf(it) } ?: SyncStatus.LOADING,
            preferences.getLong(LAST_SYNC_DATE)?.let { LocalDateTime.MIN.plusSeconds(it) },
            preferences.getString(ERROR_TEXT)
        )
    }
}