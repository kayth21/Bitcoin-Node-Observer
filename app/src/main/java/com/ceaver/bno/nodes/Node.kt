package com.ceaver.bno.nodes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "node")
data class Node(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "port") val port: Int,
    @ColumnInfo(name = "status") val status: NodeStatus
)