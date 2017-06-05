package com.eericxu.wallpaper.service;

import android.content.Intent;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.eericxu.wallpaper.animator.DefAnim;

public class WallpaperServer extends WallpaperService {
    public WallpaperServer() {

    }

    @Override
    public Engine onCreateEngine() {
        return new MEngine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public class MEngine extends Engine {

        private int desiredWidth;
        private int desiredHeight;
        private Wallpaper wallpaper;

        @Override
        public void onCreate(final SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            wallpaper = createWallpaper();
            wallpaper.onCreate(this);
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

        public int getDesiredHeight() {
            return desiredHeight;
        }

        public int getDesiredWidth() {
            return desiredWidth;
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            wallpaper.onVisibility(visible);
        }

        @Override
        public void onDestroy() {
            wallpaper.destroy();
        }
    }

    private Wallpaper createWallpaper() {
        return new DefAnim();
    }
}
