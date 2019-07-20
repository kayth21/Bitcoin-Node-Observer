package com.ceaver.bno.nodes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ceaver.bno.bitnodes.BitnodesNode
import com.ceaver.bno.extensions.asLocalDateTime
import com.ceaver.bno.network.NetworkStatus
import com.ceaver.bno.network.Response
import java.time.LocalDateTime

@Entity(tableName = "node")
data class Node(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "port") val port: Int,
    @ColumnInfo(name = "nodeStatus") val nodeStatus: NodeStatus = NodeStatus.UNKNOWN,
    @ColumnInfo(name = "networkStatus") val networkStatus: NetworkStatus = NetworkStatus.NORMAL,
    @ColumnInfo(name = "errorMessage") val errorMessage: String? = null,
    @ColumnInfo(name = "protocolVersion") val protocolVersion: String? = null,
    @ColumnInfo(name = "userAgent") val userAgent: String? = null,
    @ColumnInfo(name = "connectedSince") val connectedSince: LocalDateTime? = null,
    @ColumnInfo(name = "services") val services: String? = null,
    @ColumnInfo(name = "height") val height: Int? = null,
    @ColumnInfo(name = "hostname") val hostname: String? = null,
    @ColumnInfo(name = "city") val city: String? = null,
    @ColumnInfo(name = "countryCode") val countryCode: String? = null,
    @ColumnInfo(name = "latitude") val latitude: String? = null,
    @ColumnInfo(name = "longitude") val longitude: String? = null,
    @ColumnInfo(name = "timezone") val timezone: String? = null,
    @ColumnInfo(name = "asn") val asn: String? = null,
    @ColumnInfo(name = "organizationName") val organizationName: String? = null
) {

    fun isLoading(): Boolean {
        return networkStatus == NetworkStatus.LOADING
    }

    fun copyForReload(): Node {
        return copy(
            networkStatus = NetworkStatus.LOADING,
            errorMessage = null
        )
    }

    fun copyFromBitnodesResponse(response: Response<BitnodesNode>): Node {
        return if (response.isSuccessful()) {
            val bitnodesNode = response.result!!
            copy(
                networkStatus = NetworkStatus.NORMAL,
                nodeStatus = NodeStatus.valueOf(bitnodesNode.status),
                protocolVersion = bitnodesNode.data[0],
                userAgent = bitnodesNode.data[1],
                connectedSince = bitnodesNode.data[2].toInt().asLocalDateTime(),
                services = bitnodesNode.data[3],
                height = bitnodesNode.data[4].toInt(),
                hostname = bitnodesNode.data[5],
                city = bitnodesNode.data[6],
                countryCode = bitnodesNode.data[7],
                latitude = bitnodesNode.data[8],
                longitude = bitnodesNode.data[9],
                timezone = bitnodesNode.data[10],
                asn = bitnodesNode.data[11],
                organizationName = bitnodesNode.data[12],
                errorMessage = null
            )
        } else {
            copy(
                networkStatus = if (response.isError()) NetworkStatus.ERROR else NetworkStatus.NORMAL,
                nodeStatus = if (response.isException()) NodeStatus.EXCEPTION else nodeStatus,
                errorMessage = response.failureText()
            )
        }
    }
}