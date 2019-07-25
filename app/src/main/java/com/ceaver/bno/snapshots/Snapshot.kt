package com.ceaver.bno.snapshots

import com.ceaver.bno.bitnodes.BitnodesSnapshot
import com.ceaver.bno.extensions.asLocalDateTime
import com.ceaver.bno.network.NetworkStatus
import com.ceaver.bno.network.Response
import java.time.LocalDateTime

data class Snapshot(
    val blockHeight: Int?,
    val totalNodes: Int?,
    val snapshotDate: LocalDateTime?,
    val networkStatus: NetworkStatus,
    val errorMessage: String? = null
) {
    fun copyForReload(): Snapshot {
        return copy(
            networkStatus = NetworkStatus.LOADING,
            errorMessage = null
        )
    }

    fun isNetworkStatusNormal(): Boolean {
        return networkStatus == NetworkStatus.NORMAL
    }

    fun copyFromBitnodesResponse(response: Response<BitnodesSnapshot>): Snapshot {
        return if (response.isSuccessful()) {
            val result = response.result!!
            copy(
                blockHeight = result.blockHeight,
                totalNodes = result.totalNodes,
                snapshotDate = result.timestamp.asLocalDateTime(),
                networkStatus = NetworkStatus.NORMAL,
                errorMessage = null
            )
        } else
            copy(
                networkStatus = if (response.isError()) NetworkStatus.ERROR else NetworkStatus.NORMAL,
                errorMessage = response.failureText()
            )
    }
}