package com.ceaver.bno.snapshots

import android.content.Context
import com.ceaver.bno.Application
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val SHARED_PREFERENCES_KEY = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences"
private const val LATEST_BLOCK = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.latestBlock"
private const val TOTAL_NODES = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.totalNodes"
private const val SNAPSHOT_DATE = "com.ceaver.bno.system.SnapshotRepository.SharedPreferences.snapshotDate"

object SnapshotRepository {

    fun update(snapshot: Snapshot) {
        Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit()
            .putInt(LATEST_BLOCK, snapshot.blockHeight)
            .putInt(TOTAL_NODES, snapshot.totalNodes)
            .putLong(SNAPSHOT_DATE, ChronoUnit.SECONDS.between(LocalDateTime.MIN, snapshot.snapshotDate))
            .apply()
    }

    fun load(): Snapshot {
        val preferences = Application.appContext!!.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return Snapshot(
            preferences.getInt(LATEST_BLOCK, -1),
            preferences.getInt(TOTAL_NODES, -1),
            LocalDateTime.MIN.plusSeconds(preferences.getLong(SNAPSHOT_DATE, -1))
        )
    }
}