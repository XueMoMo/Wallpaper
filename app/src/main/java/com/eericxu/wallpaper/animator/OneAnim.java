package com.eericxu.wallpaper.animator;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eericxu.wallpaper.service.Wallpaper;
import com.eericxu.wallpaper.service.WallpaperServer;
import com.eericxu.wallpaper.utils.MathT;

/**
 * Created by Eericxu on 2017-08-04.
 */

public class OneAnim implements Wallpaper {

	private Canvas canvas;
	private ValueAnimator valueAnimator;
	private Paint paint;
	private SparseArray<Line> lines = new SparseArray<>();
	private WallpaperServer.MEngine engine;

	@Override
	public void onCreate(final WallpaperServer.MEngine engine) {
		this.engine = engine;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(5);
		initLines();
		valueAnimator = new ValueAnimator().setDuration(1500);
		int[] values = {0, 360};
		valueAnimator.setIntValues(values);
		valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
		valueAnimator.setRepeatMode(ValueAnimator.RESTART);
		valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            double sin = MathT.sin(value);
            double cos = MathT.cos(value);
            canvas = engine.getSurfaceHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.GRAY);
                int r = 100;
                int x = engine.getDesiredWidth() / 2;
                int y = engine.getDesiredHeight() / 2;
                int yOff = (int) (r * sin);
                int xOff = (int) (r * cos);
                canvas.drawLine(x - xOff, y - yOff, x + xOff, y + yOff, paint);
            }
            if (canvas != null) {
                engine.getSurfaceHolder().unlockCanvasAndPost(canvas);
                canvas = null;
            }
        });
	}

	private void initLines() {
		for (int i = 0; i < 1; i++) {
			Line line = new Line();
			line.x1 = 100;
			line.y1 = 1000;
			line.x2 = 1000;
			line.y2 = 1000;
			lines.put(i, line);
		}
	}

	@Override
	public void destroy() {
		valueAnimator.end();
		if (canvas != null && engine != null) {
			engine.getSurfaceHolder().unlockCanvasAndPost(canvas);
			canvas = null;
		}
	}

	@Override
	public void onVisibility(boolean visible) {
		if (visible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				if (valueAnimator.isPaused()) {
					valueAnimator.resume();
				} else if (!valueAnimator.isRunning()) {
					valueAnimator.start();
				}
			}
		} else {
			if (valueAnimator.isRunning()) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					valueAnimator.pause();
				}
			}
		}
	}

	private class Line {
		public int x1;
		public int y1;
		public int x2;
		public int y2;
	}
}
