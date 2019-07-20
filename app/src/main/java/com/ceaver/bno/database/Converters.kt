package com.ceaver.bno.database

import android.arch.persistence.room.TypeConverter
import com.ceaver.bno.network.NetworkStatus
import com.ceaver.bno.nodes.NodeStatus
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Converters {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): Long? = localDateTime?.let { ChronoUnit.SECONDS.between(LocalDateTime.MIN, localDateTime) }

    @TypeConverter
    fun toLocalDateTime(long: Long?): LocalDateTime? = long?.let { LocalDateTime.MIN.plusSeconds(it) }

    @TypeConverter
    fun fromNodeStatus(nodeStatus: NodeStatus?): String? = nodeStatus?.name

    @TypeConverter
    fun toNodeStatus(string: String?): NodeStatus? = string?.let { NodeStatus.valueOf(it) }

    @TypeConverter
    fun fromNetworkStatus(networkStatus: NetworkStatus?): String? = networkStatus?.name

    @TypeConverter
    fun toNetworkStatus(string: String?): NetworkStatus? = string?.let { NetworkStatus.valueOf(it) }
}