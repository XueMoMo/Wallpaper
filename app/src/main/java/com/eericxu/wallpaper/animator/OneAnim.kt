package com.eericxu.wallpaper.animator

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.SparseArray
import android.view.animation.AccelerateDecelerateInterpolator
import com.eericxu.wallpaper.service.Wallpaper
import com.eericxu.wallpaper.service.WallpaperServer.MEngine
import com.eericxu.wallpaper.utils.MathT

/**
 * Created by Eericxu on 2017-08-04.
 */
class OneAnim : Wallpaper {
    private lateinit var valueAnimator: ValueAnimator
    private var paint: Paint? = null
    private val lines = SparseArray<Line>()
    private lateinit var engine: MEngine

    override fun onCreate(engine: MEngine) {
        this.engine = engine
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.color = Color.BLUE
        paint!!.strokeWidth = 5f
        initLines()
        valueAnimator = ValueAnimator().setDuration(1500)
        val values = intArrayOf(0, 360)
        valueAnimator.setIntValues(*values)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.RESTART
        valueAnimator.addUpdateListener { animation: ValueAnimator ->
            val value = animation.animatedValue as Int
            val sin = MathT.sin(value)
            val cos = MathT.cos(value)
            val canvas = engine.surfaceHolder.lockCanvas()
            if (canvas != null) {
                canvas.drawColor(Color.BLACK)
                val r = 100
                val x = engine.desiredWidth / 2
                val y = engine.desiredHeight / 2
                val yOff = (r * sin).toInt()
                val xOff = (r * cos).toInt()
                canvas.drawLine(
                    (x - xOff).toFloat(),
                    (y - yOff).toFloat(),
                    (x + xOff).toFloat(),
                    (y + yOff).toFloat(),
                    paint!!
                )
                engine.surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
        valueAnimator.start()
    }

    private fun initLines() {
        for (i in 0..0) {
            val line: Line = Line()
            line.x1 = 100
            line.y1 = 1000
            line.x2 = 1000
            line.y2 = 1000
            lines.put(i, line)
        }
    }

    override fun destroy() {
        valueAnimator.end()
    }

    override fun onVisibility(visible: Boolean) {
        valueAnimator.apply {
            this.pause()
            if (visible && this.isPaused) this.resume()
            else if(!visible&& this.isRunning) this.pause()
        }
    }

    private inner class Line {
        var x1: Int = 0
        var y1: Int = 0
        var x2: Int = 0
        var y2: Int = 0
    }
}
