package com.ceaver.bno.bitnodes

import com.ceaver.bno.network.Response

object BitnodesRepository {

    @Suppress("UNCHECKED_CAST")
    fun lookupLatestSnapshot(): Response<BitnodesSnapshot> {
        val response = BitnodesApi.lookupSnapshots()
        return if (response.isSuccessful())
            Response.success(response.result!!.results[0])
        else
            response as Response<BitnodesSnapshot>
    }

    fun lookupNode(ip: String, port: Int): Response<BitnodesNode> {
        return BitnodesApi.lookupNodeDetails(ip, port)
    }

    fun lookupPeerIndex(ip: String, port: Int): Response<BitnodesPeerIndex> {
        return BitnodesApi.lookupPeerIndex(ip, port)
    }
}