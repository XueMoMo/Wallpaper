package com.eericxu.wallpaper.service

import android.graphics.Canvas
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.eericxu.wallpaper.service.WallpaperServer.MEngine
import java.util.Timer
import java.util.TimerTask

open class HandlerBaseWallpaper : Wallpaper {
    private lateinit var timer: Handler
    private var isVisible: Boolean = false
    private val Interval = 16L
    private val IntervalID = 1
    open lateinit var engine: MEngine
    override fun onCreate(engine: MEngine) {
        this.engine = engine;
        timer = Handler(Looper.getMainLooper(), Handler.Callback {
            drawFrame(engine, false)
            timer.sendEmptyMessageDelayed(IntervalID, Interval)
            return@Callback true;
        })
        timer.sendEmptyMessageDelayed(IntervalID, Interval)
    }

    open fun drawFrame(engine: MEngine, hardware: Boolean = false) {
        if (isVisible) {
            val needHardware = hardware && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            val canvas =
                if (needHardware) engine.surfaceHolder.lockHardwareCanvas() else engine.surfaceHolder.lockCanvas()
            if (canvas != null) {
                onFrame(canvas)
                engine.surfaceHolder.unlockCanvasAndPost(canvas)
            }

        }
    }

    override fun destroy() {
        timer.removeMessages(IntervalID)
    }

    override fun onVisibility(visible: Boolean) {
        isVisible = visible
    }

    open fun onFrame(canvas: Canvas) {
    }
}
