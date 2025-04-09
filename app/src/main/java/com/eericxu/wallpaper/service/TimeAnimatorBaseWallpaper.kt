package com.eericxu.wallpaper.service

import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.animation.LinearInterpolator
import com.eericxu.wallpaper.service.WallpaperServer.MEngine

open class TimeAnimatorBaseWallpaper : Wallpaper {

    private lateinit var anim: TimeAnimator
    var isVisible: Boolean = false
    override fun onCreate(engine: MEngine) {
        anim = TimeAnimator()
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = ValueAnimator.INFINITE
        anim.setDuration((1 * 1000).toLong())
        anim.setTimeListener { _: TimeAnimator?, total: Long, delta: Long ->
            if (isVisible) {
                val canvas = engine.surfaceHolder.lockCanvas()
                if (canvas != null) {
                    onFrame(canvas)
                    engine.surfaceHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
        anim.start()
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
