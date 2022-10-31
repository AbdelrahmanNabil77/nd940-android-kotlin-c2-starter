package com.udacity.asteroidradar.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class AppUtils {
    companion object {
        fun hasInternetConnection(app: Application): Boolean {
            val connectivityManager = app.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }

        fun showDialog(title: String, msg: String, finish: Boolean, activity: Activity, isFragmentAdded:Boolean) {
            if (isFragmentAdded) {
                if (!finish) {
                    val dialog = AlertDialog.Builder(activity).setTitle(title).setMessage(msg)
                        .setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                        }.setCancelable(false).show()
                } else {
                    val dialog = AlertDialog.Builder(activity).setTitle(title).setMessage(msg)
                        .setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                            activity.onBackPressed()
                        }.setCancelable(false).show()
                }
            }
        }
    }
}