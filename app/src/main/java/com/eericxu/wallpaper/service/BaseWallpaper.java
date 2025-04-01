package com.eericxu.wallpaper.service;

import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.animation.LinearInterpolator;

import java.util.Timer;

public class BaseWallpaper implements Wallpaper{
    public WallpaperServer.MEngine engine;
    public Canvas canvas;
    private TimeAnimator anim;
    private Timer timer = new Timer(true);
    public boolean isVisible;
    @Override
    public void onCreate(WallpaperServer.MEngine engine) {
        this.engine = engine;
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if(isVisible){
//                    canvas = engine.getSurfaceHolder().lockCanvas();
//                    onFrame(canvas);
//                    engine.getSurfaceHolder().unlockCanvasAndPost(canvas);
//                }
//            }
//        }, 0,16);
        anim = new TimeAnimator();
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(1 * 1000);
//        anim.setCurrentFraction();
        anim.setTimeListener((timeAnimator, total, delta) -> {
            if (isVisible) {
                canvas = engine.getSurfaceHolder().lockCanvas();
                onFrame(canvas, total, delta);
                engine.getSurfaceHolder().unlockCanvasAndPost(canvas);
            }
        });
        anim.start();
    }

    @Override
    public void destroy() {
        anim.cancel();
    }

    @Override
    public void onVisibility(boolean visible) {
        isVisible = visible;
        if(visible){
          if(anim.isPaused()) anim.resume();
        } else {
          if(anim.isRunning()) anim.pause();
        }
    }

    public void onFrame(Canvas canvas, long total, long delta){

    }
}
