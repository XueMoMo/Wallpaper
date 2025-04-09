package com.eericxu.wallpaper.service

import android.graphics.Canvas
import com.eericxu.wallpaper.service.WallpaperServer.MEngine

abstract class BaseWallpaper : Wallpaper {
    private var isVisible: Boolean = false
    lateinit var engine: MEngine;
    override fun onCreate(engine: MEngine) {
        this.engine = engine
    }

    override fun destroy() {
    }

    override fun onVisibility(visible: Boolean) {
        isVisible = visible
    }

    open fun drawFrame() {
        if (isVisible) {
            val canvas = engine.surfaceHolder.lockCanvas()
            if (canvas != null) {
                onFrame(canvas)
                engine.surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }

    abstract fun onFrame(canvas: Canvas)
}
