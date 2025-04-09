package com.eericxu.wallpaper.animator

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.AnticipateOvershootInterpolator
import com.eericxu.wallpaper.service.Wallpaper
import com.eericxu.wallpaper.service.WallpaperServer.MEngine

/**
 * Created by Eericxu on 2017-06-05.
 */
class DefAnim : Wallpaper {
    private lateinit var valueAnimator: ValueAnimator
    private lateinit var paint: Paint
    private lateinit var engine: MEngine

    override fun onCreate(e: MEngine) {
        this.engine = e
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLUE
        valueAnimator = ValueAnimator().setDuration(600)
        val values = intArrayOf(1, 300)
        valueAnimator.setIntValues(*values)
        valueAnimator.interpolator = AnticipateOvershootInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val holder = engine.surfaceHolder
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                canvas.drawColor(Color.BLACK)
                canvas.drawCircle(
                    engine.desiredWidth / 2.0f,
                    engine.desiredHeight / 2.0f + value,
                    50f,
                    paint
                )
                holder.unlockCanvasAndPost(canvas)
            }
        }
        valueAnimator.start()
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
}
