package com.eericxu.wallpaper.service;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.eericxu.wallpaper.animator.DefAnim;
import com.eericxu.wallpaper.animator.OneAnim;
import com.eericxu.wallpaper.utils.Config;

public class WallpaperServer extends WallpaperService {
	private MEngine mEngine;
	public static final String action_receiver = "update.wallpaper";

	public WallpaperServer() {

	}

	@Override
	public ComponentName startService(Intent service) {
		return super.startService(service);
	}

	@Override
	public Engine onCreateEngine() {
		mEngine = new MEngine(this);
		return mEngine;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		registerReceiver(new Receiver(this), new IntentFilter(action_receiver));
		return START_STICKY;
	}

	public void onChange() {
		if (mEngine != null)
			mEngine.change();
	}

	public class MEngine extends Engine {

		private int desiredWidth;
		private int desiredHeight;
		private Wallpaper wallpaper;
		public Context context;

		public MEngine(Context context) {
			this.context = context;
		}

		public void change() {
			if (wallpaper != null)
				wallpaper.destroy();
			wallpaper = createWallpaper();
			wallpaper.onCreate(this);
		}

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
		String wallpaper = Config.getWallpaper(this);
		Wallpaper w = null;
		switch (wallpaper) {
			case "anim":
				w = new DefAnim();
				break;
			case "web":
				w = new OneAnim();
				break;
		}
		if (w == null)
			w = new DefAnim();
		return w;
	}

}
