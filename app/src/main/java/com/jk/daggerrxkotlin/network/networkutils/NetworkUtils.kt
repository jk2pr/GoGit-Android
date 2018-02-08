package com.jk.daggerrxkotlin.network.networkutils

import android.content.Context
import android.net.ConnectivityManager


/**
 * Created by Jitendra on 08/11/2017.
 */
class NetworkUtils {

    companion object {
        val TAG=NetworkUtils::class.java.simpleName
        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null
        }


        fun hasActiveInternetConnection(context: Context): Boolean {
           return  (isNetworkAvailable(context))
//                try {
//                    val urlc = URL("http://www.google.com").openConnection() as HttpURLConnection
//                    urlc.setRequestProperty("User-Agent", "Test")
//                    urlc.setRequestProperty("Connection", "close")
//                    urlc.setConnectTimeout(1500)
//                    urlc.connect()
//                    return (urlc.responseCode == 200)
//                } catch (e: IOException) {
//                    Log.e(TAG, "Error checking internet connection", e)
//                }
//            } else {
//                Log.d(TAG, "No network available!")
//            }
//            return false
        }
    }
}