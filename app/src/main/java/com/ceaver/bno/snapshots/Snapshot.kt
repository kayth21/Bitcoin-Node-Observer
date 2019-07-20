package com.ceaver.bno.snapshots

import com.ceaver.bno.bitnodes.BitnodesSnapshot
import com.ceaver.bno.extensions.asLocalDateTime
import java.time.LocalDateTime

data class Snapshot(
    val blockHeight: Int,
    val totalNodes: Int,
    val snapshotDate: LocalDateTime
) {
    constructor(bitnodesSnapshot: BitnodesSnapshot) : this(
        bitnodesSnapshot.blockHeight,
        bitnodesSnapshot.totalNodes,
        bitnodesSnapshot.timestamp.asLocalDateTime()
    )
}