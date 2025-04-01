package com.eericxu.wallpaper.web;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eericxu.wallpaper.R;
import com.eericxu.wallpaper.service.BaseWallpaper;
import com.eericxu.wallpaper.service.WallpaperServer;

/**
 * Created by Eericxu on 2017-06-05.
 */

public class DefWeb extends BaseWallpaper {
    private WebView webView;
    private Paint paint;
    private boolean isLoaded = false;

    @Override
    public void onCreate(WallpaperServer.MEngine engine) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        webView = ((WebView) LayoutInflater.from(engine.context).inflate(R.layout.web, null));
        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        settings.setBlockNetworkImage(true);//解决图片不显示
        // 这样的话如果你的PC网页里面没有设置 meta标签 viewport的缩放设置也没有关系。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkLoads(true);
        settings.setEnableSmoothTransition(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                isLoaded = newProgress == 100;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                if (handler != null) {
//                    handler.cancel();
                    handler.proceed();
                }
            }

        });
//        webView.getDisplay().
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        int width = engine.getDesiredWidth(), height = engine.getDesiredHeight();
        Log.i("DefWeb: size:", width + "," + height);
        webView.measure(width, height);
        webView.layout(0, 0, width, height);
        webView.loadUrl("file:///android_asset/one.html");
        super.onCreate(engine);


    }

    @Override
    public void onFrame(Canvas canvas, long total, long delta) {
//        canvas.drawBitmap(webView.getDrawingCache(true),0, 0, paint);
        if (isLoaded) {
//            webView.draw(canvas);
        } else {
            canvas.drawColor(Color.BLACK);
        }
    }

}
