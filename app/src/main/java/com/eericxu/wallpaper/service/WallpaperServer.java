package com.eericxu.wallpaper.service;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.view.animation.AccelerateInterpolator;

public class WallpaperServer extends WallpaperService {
    public WallpaperServer() {
    }

    @Override
    public Engine onCreateEngine() {
        return new MEngine();
    }


    private class MEngine extends Engine {
        private Canvas canvas;
        private ValueAnimator valueAnimator;
        private Paint paint;
        private int desiredWidth;
        private int desiredHeight;

        @Override
        public void onCreate(final SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            canvas = surfaceHolder.lockCanvas();
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLUE);
            valueAnimator = new ValueAnimator().setDuration(600);
            int[] values = {1, 300};
            valueAnimator.setIntValues(values);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    SurfaceHolder holder = getSurfaceHolder();
                    canvas = holder.lockCanvas();
                    if (canvas != null) {
//                        Log.i("wallpaper:","h:"+desiredHeight+"     w:"+desiredWidth);
                        canvas.drawColor(Color.GRAY);
                        canvas.drawCircle(desiredWidth / 2, desiredHeight / 2 + value, 50, paint);
//                        canvas.draw
//                        canvas.save();
                    }
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }
            });
        }

        @Override
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            super.onDesiredSizeChanged(desiredWidth, desiredHeight);
            this.desiredWidth = desiredWidth;
            this.desiredHeight = desiredHeight;
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            desiredWidth = width;
            desiredHeight = height;
        }


        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (valueAnimator.isPaused()) {
                        valueAnimator.resume();
                    } else if (!valueAnimator.isRunning()) {
                        valueAnimator.start();
                    }

                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (valueAnimator.isRunning())
                        valueAnimator.pause();
                }
            }
        }
    }
}
