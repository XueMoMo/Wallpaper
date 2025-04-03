package com.eericxu.wallpaper.service

import android.animation.TimeAnimator
import android.graphics.Canvas
import com.eericxu.wallpaper.service.WallpaperServer.MEngine
import java.util.Timer
import java.util.TimerTask

open class TimerBaseWallpaper : Wallpaper {
    lateinit var engine: MEngine
    lateinit var canvas: Canvas
    private lateinit var anim: TimeAnimator
    private val timer = Timer(true)
    private var isVisible: Boolean = false
    override fun onCreate(engine: MEngine) {
        this.engine = engine
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (isVisible) {
                    canvas = engine.surfaceHolder.lockCanvas()
                    onFrame(canvas)
                    engine.surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }, 0, 16);
    }

    override fun destroy() {
        anim.cancel()
    }

    override fun onVisibility(visible: Boolean) {
        isVisible = visible
        if (visible) {
            if (anim.isPaused) anim.resume()
        } else {
            if (anim.isRunning) anim.pause()
        }
    }

    open fun onFrame(canvas: Canvas) {
    }
}
