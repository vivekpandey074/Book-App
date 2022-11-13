package com.example.learningconcepts.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager.NetworkInfoListener

class ConnectionManager {

    fun checkConnectivity(context: Context):Boolean{

        val connectiviyManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectiviyManager.activeNetworkInfo
        if(activeNetwork?.isConnected!=null)
        {
            return activeNetwork.isConnected

        }
        else{
            return false
        }
    }
}