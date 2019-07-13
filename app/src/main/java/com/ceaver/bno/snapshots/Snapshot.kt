package com.ceaver.bno.snapshots

import java.time.LocalDateTime

data class Snapshot(
    val blockHeight: Int,
    val totalNodes: Int,
    val snapshotDate: LocalDateTime
)