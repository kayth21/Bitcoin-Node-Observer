package com.ceaver.bno.nodes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.ceaver.bno.bitnodes.BitnodesNode
import com.ceaver.bno.bitnodes.BitnodesPeerIndex
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
    // node details
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
    @ColumnInfo(name = "organizationName") val organizationName: String? = null,
    // peer index
    @ColumnInfo(name = "rank") val rank: Int? = null,
    @ColumnInfo(name = "peerIndex") val peerIndex: Double? = null,
    @ColumnInfo(name = "vi") val vi: Double? = null,
    @ColumnInfo(name = "si") val si: Double? = null,
    @ColumnInfo(name = "hi") val hi: Double? = null,
    @ColumnInfo(name = "ai") val ai: Double? = null,
    @ColumnInfo(name = "pi") val pi: Double? = null,
    @ColumnInfo(name = "dli") val dli: Double? = null,
    @ColumnInfo(name = "dui") val dui: Double? = null,
    @ColumnInfo(name = "wli") val wli: Double? = null,
    @ColumnInfo(name = "wui") val wui: Double? = null,
    @ColumnInfo(name = "mli") val mli: Double? = null,
    @ColumnInfo(name = "mui") val mui: Double? = null,
    @ColumnInfo(name = "nsi") val nsi: Double? = null,
    @ColumnInfo(name = "ni") val ni: Double? = null,
    @ColumnInfo(name = "bi") val bi: Double? = null
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

    fun copyFromBitnodesResponse(nodeInfoResponse: Response<BitnodesNode>, peerIndexResponse: Response<BitnodesPeerIndex>): Node {
        return if (nodeInfoResponse.isSuccessful() && peerIndexResponse.isSuccessful()) {
            val bitnodesNode = nodeInfoResponse.result!!
            val bitnodesPeerIndex = peerIndexResponse.result!!
            copy(
                networkStatus = NetworkStatus.NORMAL,
                nodeStatus = NodeStatus.valueOf(bitnodesNode.status),
                errorMessage = null,
                // node infos
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
                // peer index
                rank = bitnodesPeerIndex.rank,
                peerIndex = bitnodesPeerIndex.peerIndex,
                vi = bitnodesPeerIndex.vi,
                si = bitnodesPeerIndex.si,
                hi = bitnodesPeerIndex.hi,
                ai = bitnodesPeerIndex.ai,
                pi = bitnodesPeerIndex.pi,
                dli = bitnodesPeerIndex.dli,
                dui = bitnodesPeerIndex.dui,
                wli = bitnodesPeerIndex.wli,
                wui = bitnodesPeerIndex.wui,
                mli = bitnodesPeerIndex.mli,
                mui = bitnodesPeerIndex.mui,
                nsi = bitnodesPeerIndex.nsi,
                ni = bitnodesPeerIndex.ni,
                bi = bitnodesPeerIndex.bi
            )
        } else {
            copy(
                networkStatus = if (nodeInfoResponse.isError() || peerIndexResponse.isError()) NetworkStatus.ERROR else NetworkStatus.NORMAL,
                nodeStatus = if (nodeInfoResponse.isException() || peerIndexResponse.isException()) NodeStatus.EXCEPTION else nodeStatus,
                errorMessage = nodeInfoResponse.failureText() ?: peerIndexResponse.failureText()
            )
        }
    }
}