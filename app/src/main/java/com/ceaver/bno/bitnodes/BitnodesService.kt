package com.ceaver.bno.bitnodes

import retrofit2.Call
import retrofit2.http.GET

interface BitnodesService {

    @GET("/api/v1/snapshots?limit=1")
    fun snapshots(): Call<BitnodesSnapshots>
}