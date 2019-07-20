package com.ceaver.bno.bitnodes

import com.google.gson.annotations.SerializedName

data class BitnodesNode(
    @SerializedName("hostname")
    val hostname: String,
    @SerializedName("address")
    val ipAddress: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Array<String>,
    @SerializedName("bitcoin_address")
    val bitcoinAddress: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("verified")
    val verified: Boolean,
    @SerializedName("mbps")
    val mbps: Double
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BitnodesNode

        if (hostname != other.hostname) return false
        if (ipAddress != other.ipAddress) return false
        if (status != other.status) return false
        if (!data.contentEquals(other.data)) return false
        if (bitcoinAddress != other.bitcoinAddress) return false
        if (url != other.url) return false
        if (verified != other.verified) return false
        if (mbps != other.mbps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hostname.hashCode()
        result = 31 * result + ipAddress.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + bitcoinAddress.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + verified.hashCode()
        result = 31 * result + mbps.hashCode()
        return result
    }
}