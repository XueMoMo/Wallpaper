package com.eericxu.wallpaper.service

import android.graphics.Canvas
import com.eericxu.wallpaper.service.WallpaperServer.MEngine
import java.util.Timer
import java.util.TimerTask

open class TimerBaseWallpaper : Wallpaper {
    private lateinit var timer: Timer
    private var isVisible: Boolean = false
    override fun onCreate(engine: MEngine) {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (isVisible) {
                    val canvas = engine.surfaceHolder.lockCanvas()
                    if (canvas != null) {
                        onFrame(canvas)
                        engine.surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }, 0, 16);
    }

    override fun destroy() {
        timer.cancel()
    }

    override fun onVisibility(visible: Boolean) {
        isVisible = visible
    }

    open fun onFrame(canvas: Canvas) {
    }
}
