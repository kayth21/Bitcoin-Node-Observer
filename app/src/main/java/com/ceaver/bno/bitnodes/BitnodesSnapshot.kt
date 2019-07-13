package com.ceaver.bno.bitnodes

import com.google.gson.annotations.SerializedName

data class BitnodesSnapshot(
    @SerializedName("url")
    val url: String,
    @SerializedName("timestamp")
    val timestamp: Int,
    @SerializedName("total_nodes")
    val totalNodes: Int,
    @SerializedName("latest_height")
    val blockHeight: Int
)