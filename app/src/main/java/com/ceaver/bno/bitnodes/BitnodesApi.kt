package com.ceaver.bno.bitnodes

import com.ceaver.bno.network.Response
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.InetAddress

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

    fun lookupNodeDetails(host: String, port: Int): Response<BitnodesNode> = try {
        mapResponse(bitnodes.nodeDetails(InetAddress.getByName(host).hostAddress, port).execute())
    } catch (e: IOException) {
        Response.error(e.toString())
    }

    fun lookupPeerIndex(host: String, port: Int): Response<BitnodesPeerIndex> = try {
        mapResponse(bitnodes.peerIndex(InetAddress.getByName(host).hostAddress, port).execute())
    } catch (e: IOException) {
        Response.error(e.toString())
    }
}

private fun <T> mapResponse(response: retrofit2.Response<T>): Response<T> =
    if (response.isSuccessful) {
        Response.success(response.body()!!)
    } else {
        val errorCode = response.code().toString()
        val errorJsonString = response.errorBody()?.string()
        val errorText = errorJsonString.let {
            try {
                JsonParser().parse(it).asJsonObject["detail"]?.asString
            } catch (e: JsonParseException) {
                null
            }
        }
        Response.exception("Error " + errorCode + ": " + (errorText ?: "failed loading detail error text"))
    }