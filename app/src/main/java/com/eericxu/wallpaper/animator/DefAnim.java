package com.eericxu.wallpaper.animator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.animation.AnticipateOvershootInterpolator;

import com.eericxu.wallpaper.service.Wallpaper;
import com.eericxu.wallpaper.service.WallpaperServer;

/**
 * Created by Eericxu on 2017-06-05.
 */

public class DefAnim implements Wallpaper {
    private Canvas canvas;
    private ValueAnimator valueAnimator;
    private Paint paint;

    public DefAnim() {
    }

    @Override
    public void onCreate(final WallpaperServer.MEngine engine) {
        SurfaceHolder surfaceHolder = engine.getSurfaceHolder();
        canvas = surfaceHolder.lockCanvas();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);

        valueAnimator = new ValueAnimator().setDuration(600);
        int[] values = {1, 300};
        valueAnimator.setIntValues(values);
        valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                SurfaceHolder holder = engine.getSurfaceHolder();
                canvas = holder.lockCanvas();
                if (canvas != null) {
//                        Log.i("wallpaper:","h:"+desiredHeight+"     w:"+desiredWidth);
                    canvas.drawColor(Color.GRAY);
                    canvas.drawCircle(engine.getDesiredWidth() / 2, engine.getDesiredHeight() / 2 + value, 50, paint);
//                        canvas.draw
//                        canvas.save();
                }
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        });
    }

    @Override
    public void destroy() {
        valueAnimator.end();
    }

    @Override
    public void onVisibility(boolean visible) {
        if (visible){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (valueAnimator.isPaused()) {
                    valueAnimator.resume();
                } else if (!valueAnimator.isRunning()) {
                    valueAnimator.start();
                }
            }
        }else {
            if (valueAnimator.isRunning()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    valueAnimator.pause();
                }
            }
        }

    }


}
