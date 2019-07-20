package com.ceaver.bno.bitnodes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BitnodesService {

    @GET("/api/v1/snapshots?limit=1")
    fun snapshots(): Call<BitnodesSnapshots>

    @GET("/api/v1/nodes/{ip}-{port}")
    fun node(@Path("ip") ip: String, @Path("port") port: Int): Call<BitnodesNode>
}