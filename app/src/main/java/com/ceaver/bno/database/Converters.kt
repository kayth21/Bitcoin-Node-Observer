package com.ceaver.bno.database

import android.arch.persistence.room.TypeConverter
import com.ceaver.bno.nodes.NodeStatus

class Converters {

    @TypeConverter
    fun fromNodeStatus(nodeStatus: NodeStatus?): String? = nodeStatus?.name

    @TypeConverter
    fun toNodeStatus(string: String?): NodeStatus? = string?.let { NodeStatus.valueOf(it) }
}