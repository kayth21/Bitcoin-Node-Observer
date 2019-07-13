package com.ceaver.bno.bitnodes

import com.google.gson.annotations.SerializedName

data class BitnodesSnapshots(
    @SerializedName("count")
    var count: Int,
    @SerializedName("next")
    var next: String,
    @SerializedName("previous")
    var previous: String,
    @SerializedName("results")
    var results: List<BitnodesSnapshot>
)