package com.eericxu.wallpaper.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.wallpaper.WallpaperService
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import com.eericxu.wallpaper.animator.DefAnim
import com.eericxu.wallpaper.animator.OneAnim
import com.eericxu.wallpaper.utils.Config
import com.eericxu.wallpaper.video.DefVideo
import com.eericxu.wallpaper.web.DefWeb
import java.util.Objects

class WallpaperServer : WallpaperService() {
    private var mEngine: MEngine? = null
    open val TAG = "WallpaperServer"
    override fun startService(service: Intent): ComponentName? {
        return super.startService(service)
    }

    override fun onCreateEngine(): Engine {
        Log.i("", "onCreateEngine: ${this.packageName} ${this.application == null}")
        mEngine = MEngine(this.application)
        return mEngine!!
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        registerReceiver(Receiver(this), IntentFilter(ACTION_RECEIVER))
        return START_STICKY
    }

    fun onChange() {
        mEngine?.change(true)
    }

    inner class MEngine(var context: Context) : Engine() {
        var desiredWidth: Int = 0
            private set
        var desiredHeight: Int = 0
            private set
        private var wallpaper: Wallpaper? = null

        fun change(force: Boolean = false) {
            wallpaper?.destroy()
            wallpaper = createWallpaper()
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
        }

        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            this.desiredWidth = desiredWidth
            this.desiredHeight = desiredHeight
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
//            Log.i(TAG, "onSurfaceCreated: ${Objects.hashCode(holder.surface)} ${Objects.hashCode(holder.surfaceFrame)}")
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            desiredWidth = width
            desiredHeight = height
            change()
//            Log.i(TAG, "onSurfaceChanged: ${Objects.hashCode(holder.surface)} ${Objects.hashCode(holder.surfaceFrame)}")
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder) {
            super.onSurfaceRedrawNeeded(holder)
//            change()
//            Log.i(TAG, "onSurfaceRedrawNeeded: ${Objects.hashCode(holder.surface)} ${Objects.hashCode(holder.surfaceFrame)}")
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder?) {
            super.onSurfaceDestroyed(holder)
            wallpaper?.destroy()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            wallpaper?.onVisibility(visible)
        }

        override fun onDestroy() {
            wallpaper?.destroy()
        }

    }

    private fun createWallpaper(): Wallpaper {
        val wallpaper = Config.getWallpaper(this)
        var w: Wallpaper? = null
        when (wallpaper) {
            "anim" -> w = DefAnim()
            "web" -> w = DefWeb()
            "one" -> w = OneAnim()
            "video" -> w = DefVideo()
        }
        if (w == null) {
            w = DefAnim()
        }
        w.onCreate(mEngine!!)
        w.onVisibility(true)
        return w
    }

    companion object {
        const val ACTION_RECEIVER: String = "update.wallpaper"
    }
}
