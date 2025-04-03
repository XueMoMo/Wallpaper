package com.eericxu.wallpaper.base

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.eericxu.wallpaper.utils.PermissionUtils

/**
 * Created by Eericxu on 2017-06-05.
 */
open class BaseActivity : AppCompatActivity() {
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionResult(this, requestCode, permissions, grantResults)
    }

    fun <T> findView(id: Int): T {
        return findViewById<View>(id) as T
    }
}
