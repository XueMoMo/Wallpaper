package com.eericxu.wallpaper.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by Eericxu on 2017/2/28.
 */
object PermissionUtils {
    private const val MY_PERMISSIONS_REQUEST = 101

    // Here, thisActivity is the current activity
    private val callbackMap: MutableMap<Activity, Callback> = HashMap()
    fun checkAndReqPermission(thisActivity: Activity, permission: String, callback: Callback) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                thisActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            callbackMap[thisActivity] = callback
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(
                    thisActivity,
                    arrayOf(permission),
                    MY_PERMISSIONS_REQUEST
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            callback.call(true)
        }
    }

    fun onRequestPermissionResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<String>?,
        grantResults: IntArray
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            val callback = callbackMap[activity]
            if (callback != null && grantResults.size > 0) {
                callback.call(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                callbackMap.remove(activity)
            }
        }
    }

    abstract class Callback {
        abstract fun call(hasPermission: Boolean)
    }
}
