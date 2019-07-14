package com.ceaver.bno.bitnodes

import com.ceaver.bno.network.Response

object BitnodesRepository {

    fun lookupLatestSnapshot(): Response<BitnodesSnapshot> {
        val response = BitnodesApi.lookupSnapshots()
        return if (response.isSuccessful())
            Response.success(response.result!!.results[0])
        else
            Response.error(response.error!!)
    }
}