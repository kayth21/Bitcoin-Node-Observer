package com.ceaver.bno.bitnodes

import com.google.gson.annotations.SerializedName

data class BitnodesPeerIndex(
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("peer_index")
    val peerIndex: String,
    @SerializedName("vi")
    val protocolVersionIndex: String,
    @SerializedName("si")
    val servicesIndex: String,
    @SerializedName("hi")
    val heightIndex: String,
    @SerializedName("ai")
    val asnIndex: String,
    @SerializedName("pi")
    val portIndex: String,
    @SerializedName("dli")
    val averageDailyLatencyIndex: String,
    @SerializedName("dui")
    val dailyUptimeIndex: String,
    @SerializedName("wli")
    val averageWeeklyLatencyIndex: String,
    @SerializedName("wui")
    val weeklyUptimeIndex: String,
    @SerializedName("mli")
    val averageMonthlyLatencyIndex: String,
    @SerializedName("mui")
    val monthlyUptimeIndex: String,
    @SerializedName("nsi")
    val networkSpeedIndex: String,
    @SerializedName("ni")
    val nodesIndex: String,
    @SerializedName("bi")
    val blockIndex: String)
