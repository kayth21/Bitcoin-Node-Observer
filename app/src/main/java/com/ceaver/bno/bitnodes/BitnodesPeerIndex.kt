package com.ceaver.bno.bitnodes

import com.google.gson.annotations.SerializedName

data class BitnodesPeerIndex(
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("peer_index")
    val peerIndex: Double,
    @SerializedName("vi")
    val vi: Double,
    @SerializedName("si")
    val si: Double,
    @SerializedName("hi")
    val hi: Double,
    @SerializedName("ai")
    val ai: Double,
    @SerializedName("pi")
    val pi: Double,
    @SerializedName("dli")
    val dli: Double,
    @SerializedName("dui")
    val dui: Double,
    @SerializedName("wli")
    val wli: Double,
    @SerializedName("wui")
    val wui: Double,
    @SerializedName("mli")
    val mli: Double,
    @SerializedName("mui")
    val mui: Double,
    @SerializedName("nsi")
    val nsi: Double,
    @SerializedName("ni")
    val ni: Double,
    @SerializedName("bi")
    val bi: Double)
