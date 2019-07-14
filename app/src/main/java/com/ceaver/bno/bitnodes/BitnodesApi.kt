package com.ceaver.bno.bitnodes

import com.ceaver.bno.network.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object BitnodesApi {

    private val bitnodes = Retrofit.Builder()
        .baseUrl("https://bitnodes.earn.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(BitnodesService::class.java)

    fun lookupSnapshots(): Response<BitnodesSnapshots> = try {
        mapResponse(bitnodes.snapshots().execute())
    } catch (e: IOException) {
        Response.error(e.toString())
    }

    private fun <T> mapResponse(response: retrofit2.Response<T>): Response<T> =
        if (response.isSuccessful)
            Response.success(response.body()!!)
        else
            Response.error(response.code().toString() + " " + response.message())
}