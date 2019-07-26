package com.ceaver.bno.snapshots

import com.ceaver.bno.bitnodes.BitnodesSnapshot
import com.ceaver.bno.extensions.asLocalDateTime
import com.ceaver.bno.network.Response
import com.ceaver.bno.network.SyncStatus
import java.time.LocalDateTime

data class Snapshot(
    val blockHeight: Int?,
    val totalNodes: Int?,
    val snapshotDate: LocalDateTime?,
    val lastSyncStatus: SyncStatus? = null,
    val lastSyncDate: LocalDateTime? = null,
    val errorMessage: String? = null
) {
    fun copyForReload(): Snapshot {
        return copy(
            lastSyncStatus = SyncStatus.LOADING,
            errorMessage = null
        )
    }

    fun isNetworkStatusNormal(): Boolean {
        return lastSyncStatus == SyncStatus.NORMAL
    }

    fun copyFromBitnodesResponse(response: Response<BitnodesSnapshot>): Snapshot {
        return if (response.isSuccessful()) {
            val result = response.result!!
            copy(
                blockHeight = result.blockHeight,
                totalNodes = result.totalNodes,
                snapshotDate = result.timestamp.asLocalDateTime(),
                lastSyncStatus = SyncStatus.NORMAL,
                lastSyncDate = LocalDateTime.now(),
                errorMessage = null
            )
        } else
            copy(
                lastSyncStatus = if (response.isError()) SyncStatus.ERROR else SyncStatus.NORMAL,
                lastSyncDate = LocalDateTime.now(),
                errorMessage = response.failureText()
            )
    }
}