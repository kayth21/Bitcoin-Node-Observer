package com.ceaver.bno.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.ceaver.bno.Application

object Network {
    fun isConnected(): Boolean {
        val connectivityManager = Application.appContext!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}