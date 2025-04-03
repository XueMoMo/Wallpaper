package com.eericxu.wallpaper

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.eericxu.wallpaper.base.BaseActivity
import com.eericxu.wallpaper.service.WallpaperServer
import com.eericxu.wallpaper.utils.Config
import com.eericxu.wallpaper.utils.PermissionUtils
import java.io.File
import java.net.URI

class MainActivity : BaseActivity(), OnItemClickListener, OnItemSelectedListener {
    private var spinner: Spinner? = null
    private var arrayAdapter: ArrayAdapter<String>? = null
    private val names = arrayOf("anim", "web", "one")

    private fun copyFile() {
        val extDir = getExternalFilesDir("")
        val files = assets.list("")
        files?.forEach {
            Log.i(this.packageName, "copyFile:  $it")
        }
        val input = assets.open("one.html")
        val output = File(extDir, "one.html").outputStream()
        var t: Int
        val buffer = ByteArray(8192)
        while ((input.read(buffer).also { t = it }) > 0) {
            output.write(buffer, 0, t)
        }
        input.close()
        output.flush()
        output.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, WallpaperServer::class.java))
        initView()
//        copyFile()
        PermissionUtils.checkAndReqPermission(this,
            "android.permission.MANAGE_EXTERNAL_STORAGE",
            object : PermissionUtils.Callback() {
                override fun call(hasPermission: Boolean) {
                }
            })
    }

    private fun initView() {
        spinner = findView(R.id.spinner)
        arrayAdapter = ArrayAdapter(this, R.layout.item_tv, R.id.tv_name, names)
        spinner!!.adapter = arrayAdapter
        spinner!!.onItemSelectedListener = this
    }

    private fun onChange(position: Int) {
        if (position < 0) return
        Config.saveWallpaper(this, names[position])
        sendBroadcast(Intent(WallpaperServer.Companion.action_receiver))
        val instance = WallpaperManager.getInstance(this)
        val wallpaperInfo = instance.wallpaperInfo
        if (wallpaperInfo == null || wallpaperInfo.serviceInfo.packageName != packageName) {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, ComponentName(
                    application, WallpaperServer::class.java
                )
            )
            startActivity(intent)
        } else {
            val mHomeIntent = Intent(Intent.ACTION_MAIN)
            mHomeIntent.addCategory(Intent.CATEGORY_HOME)
            mHomeIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
            startActivity(mHomeIntent)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        onChange(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}
