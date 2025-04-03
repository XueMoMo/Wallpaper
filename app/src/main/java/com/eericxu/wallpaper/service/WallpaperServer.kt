package com.eericxu.wallpaper.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.eericxu.wallpaper.animator.DefAnim
import com.eericxu.wallpaper.animator.OneAnim
import com.eericxu.wallpaper.utils.Config
import com.eericxu.wallpaper.web.DefWeb

class WallpaperServer : WallpaperService() {
    private var mEngine: MEngine? = null
    override fun startService(service: Intent): ComponentName? {
        return super.startService(service)
    }

    override fun onCreateEngine(): Engine {
        mEngine = MEngine(this)
        return mEngine!!
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        registerReceiver(Receiver(this), IntentFilter(action_receiver))
        return START_STICKY
    }

    fun onChange() {
        mEngine?.change()
    }

    inner class MEngine(var context: Context) : Engine() {
        var desiredWidth: Int = 0
            private set
        var desiredHeight: Int = 0
            private set
        private var wallpaper: Wallpaper? = null

        fun change() {
            wallpaper?.destroy()
            wallpaper = createWallpaper()
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            wallpaper = createWallpaper()
        }

        override fun onDesiredSizeChanged(desiredWidth: Int, desiredHeight: Int) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight)
            this.desiredWidth = desiredWidth
            this.desiredHeight = desiredHeight
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            desiredWidth = width
            desiredHeight = height
        }

        override fun onSurfaceRedrawNeeded(holder: SurfaceHolder) {
            super.onSurfaceRedrawNeeded(holder)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            wallpaper!!.onVisibility(visible)
        }

        override fun onDestroy() {
            wallpaper!!.destroy()
        }
    }

    private fun createWallpaper(): Wallpaper {
        val wallpaper = Config.getWallpaper(this)
        var w: Wallpaper? = null
        when (wallpaper) {
            "anim" -> w = DefAnim()
            "web" -> w = DefWeb()
            "one" -> w = OneAnim()
        }
        if (w == null) {
            w = DefAnim()
        }
        w.onCreate(mEngine!!)
        w.onVisibility(true)
        return w
    }

    companion object {
        const val action_receiver: String = "update.wallpaper"
    }
}
