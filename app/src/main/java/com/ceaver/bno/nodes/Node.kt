package com.ceaver.bno.nodes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ceaver.bno.bitnodes.BitnodesNode
import com.ceaver.bno.bitnodes.BitnodesPeerIndex
import com.ceaver.bno.extensions.asLocalDateTime
import com.ceaver.bno.network.Response
import com.ceaver.bno.network.SyncStatus
import java.time.LocalDateTime

@Entity(tableName = "node")
data class Node(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "port") val port: Int,
    @ColumnInfo(name = "nodeStatus") val nodeStatus: NodeStatus? = null,
    @ColumnInfo(name = "lastSyncStatus") val lastSyncStatus: SyncStatus? = null,
    @ColumnInfo(name = "lastSyncDate") val lastSyncDate: LocalDateTime? = null,
    @ColumnInfo(name = "errorMessage") val errorMessage: String? = null,
    // node details
    @ColumnInfo(name = "protocolVersion") val protocolVersion: String? = null,
    @ColumnInfo(name = "userAgent") val userAgent: String? = null,
    @ColumnInfo(name = "connectedSince") val connectedSince: LocalDateTime? = null,
    @ColumnInfo(name = "services") val services: Int? = null,
    @ColumnInfo(name = "height") val height: Int? = null,
    @ColumnInfo(name = "hostname") val hostname: String? = null,
    @ColumnInfo(name = "city") val city: String? = null,
    @ColumnInfo(name = "countryCode") val countryCode: String? = null,
    @ColumnInfo(name = "latitude") val latitude: String? = null,
    @ColumnInfo(name = "longitude") val longitude: String? = null,
    @ColumnInfo(name = "timezone") val timezone: String? = null,
    @ColumnInfo(name = "asn") val asn: String? = null,
    @ColumnInfo(name = "organizationName") val organizationName: String? = null,
    // peer index
    @ColumnInfo(name = "rank") val rank: Int? = null,
    @ColumnInfo(name = "peerIndex") val peerIndex: String? = null,
    @ColumnInfo(name = "protocolVersionIndex") val protocolVersionIndex: String? = null,
    @ColumnInfo(name = "servicesIndex") val servicesIndex: String? = null,
    @ColumnInfo(name = "heightIndex") val heightIndex: String? = null,
    @ColumnInfo(name = "asnIndex") val asnIndex: String? = null,
    @ColumnInfo(name = "portIndex") val portIndex: String? = null,
    @ColumnInfo(name = "averageDailyLatencyIndex") val averageDailyLatencyIndex: String? = null,
    @ColumnInfo(name = "dailyUptimeIndex") val dailyUptimeIndex: String? = null,
    @ColumnInfo(name = "averageWeeklyLatencyIndex") val averageWeeklyLatencyIndex: String? = null,
    @ColumnInfo(name = "weeklyUptimeIndex") val weeklyUptimeIndex: String? = null,
    @ColumnInfo(name = "averageMonthlyLatencyIndex") val averageMonthlyLatencyIndex: String? = null,
    @ColumnInfo(name = "monthlyUptimeIndex") val monthlyUptimeIndex: String? = null,
    @ColumnInfo(name = "networkSpeedIndex") val networkSpeedIndex: String? = null,
    @ColumnInfo(name = "nodesIndex") val nodesIndex: String? = null,
    @ColumnInfo(name = "blockIndex") val blockIndex: String? = null
) {

    fun isLoading(): Boolean {
        return lastSyncStatus == SyncStatus.LOADING
    }

    fun copyForReload(): Node {
        return copy(
            lastSyncStatus = SyncStatus.LOADING,
            lastSyncDate = LocalDateTime.now(),
            errorMessage = null
        )
    }

    fun copyFromBitnodesResponse(nodeInfoResponse: Response<BitnodesNode>, peerIndexResponse: Response<BitnodesPeerIndex>): Node {
        return if (nodeInfoResponse.isSuccessful() && peerIndexResponse.isSuccessful()) {
            val bitnodesNode = nodeInfoResponse.result!!
            val bitnodesPeerIndex = peerIndexResponse.result!!
            copy(
                lastSyncStatus = SyncStatus.NORMAL,
                lastSyncDate = LocalDateTime.now(),
                nodeStatus = NodeStatus.valueOf(bitnodesNode.status),
                errorMessage = null,
                // node infos
                protocolVersion = bitnodesNode.data[0],
                userAgent = bitnodesNode.data[1],
                connectedSince = bitnodesNode.data[2].toInt().asLocalDateTime(),
                services = bitnodesNode.data[3].toInt(),
                height = bitnodesNode.data[4].toInt(),
                hostname = bitnodesNode.data[5],
                city = bitnodesNode.data[6],
                countryCode = bitnodesNode.data[7],
                latitude = bitnodesNode.data[8],
                longitude = bitnodesNode.data[9],
                timezone = bitnodesNode.data[10],
                asn = bitnodesNode.data[11],
                organizationName = bitnodesNode.data[12],
                // peer index
                rank = bitnodesPeerIndex.rank,
                peerIndex = bitnodesPeerIndex.peerIndex,
                protocolVersionIndex = bitnodesPeerIndex.protocolVersionIndex,
                servicesIndex = bitnodesPeerIndex.servicesIndex,
                heightIndex = bitnodesPeerIndex.heightIndex,
                asnIndex = bitnodesPeerIndex.asnIndex,
                portIndex = bitnodesPeerIndex.portIndex,
                averageDailyLatencyIndex = bitnodesPeerIndex.averageDailyLatencyIndex,
                dailyUptimeIndex = bitnodesPeerIndex.dailyUptimeIndex,
                averageWeeklyLatencyIndex = bitnodesPeerIndex.averageWeeklyLatencyIndex,
                weeklyUptimeIndex = bitnodesPeerIndex.weeklyUptimeIndex,
                averageMonthlyLatencyIndex = bitnodesPeerIndex.averageMonthlyLatencyIndex,
                monthlyUptimeIndex = bitnodesPeerIndex.monthlyUptimeIndex,
                networkSpeedIndex = bitnodesPeerIndex.networkSpeedIndex,
                nodesIndex = bitnodesPeerIndex.nodesIndex,
                blockIndex = bitnodesPeerIndex.blockIndex
            )
        } else {
            copy(
                lastSyncStatus = if (nodeInfoResponse.isError() || peerIndexResponse.isError()) SyncStatus.ERROR else SyncStatus.NORMAL,
                lastSyncDate = LocalDateTime.now(),
                nodeStatus = if (nodeInfoResponse.isException() || peerIndexResponse.isException()) NodeStatus.EXCEPTION else nodeStatus,
                errorMessage = nodeInfoResponse.failureText() ?: peerIndexResponse.failureText()
            )
        }
    }
}