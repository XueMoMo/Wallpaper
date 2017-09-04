package com.eericxu.wallpaper.web;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.eericxu.wallpaper.R;
import com.eericxu.wallpaper.service.Wallpaper;
import com.eericxu.wallpaper.service.WallpaperServer;

/**
 * Created by Eericxu on 2017-06-05.
 */

public class DefWeb implements Wallpaper {
	private WebView webView;

	@Override
	public void onCreate(WallpaperServer.MEngine engine) {
//		engine.getSurfaceHolder().
		webView = ((WebView) LayoutInflater.from(engine.context).inflate(R.layout.web, null));
		WebSettings settings = webView.getSettings();
		settings.setAllowContentAccess(true);
		settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
		settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
		// 这样的话如果你的PC网页里面没有设置 meta标签 viewport的缩放设置也没有关系。
		settings.setBlockNetworkImage(true);//解决图片不显示
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		/**
		 * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
		 * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
		 */
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
		SurfaceHolder surfaceHolder = engine.getSurfaceHolder();
		Canvas canvas = surfaceHolder.lockCanvas();
		Surface surface = surfaceHolder.getSurface();
		webView.measure(engine.getDesiredWidth(), engine.getDesiredHeight());
		webView.layout(0, 0, engine.getDesiredWidth(), engine.getDesiredHeight());
		if (canvas != null)
			webView.draw(canvas);
		webView.loadUrl("http://www.baidu.com");
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void onVisibility(boolean visible) {

	}
}
