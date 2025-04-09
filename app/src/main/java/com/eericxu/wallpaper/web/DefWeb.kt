package com.eericxu.wallpaper.web

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.http.SslError
import android.os.Build
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.eericxu.wallpaper.service.HandlerBaseWallpaper
import com.eericxu.wallpaper.service.WallpaperServer.MEngine

/**
 * Created by Eericxu on 2017-06-05.
 */
class DefWeb() : HandlerBaseWallpaper() {
    private lateinit var webView: WebView
    private lateinit var paint: Paint
    private var isLoaded = false
    val TAG = "DefWeb"

    override fun onCreate(engine: MEngine) {
        super.onCreate(engine)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.textSize = 40f
        paint.strokeWidth = 5f
        WebView.enableSlowWholeDocumentDraw()
        webView = WebView(engine.context)
        val settings = webView.settings
        settings.allowFileAccess = true
//        settings.allowFileAccessFromFileURLs = true
        settings.useWideViewPort = true //设置此属性，可任意比例缩放
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        settings.loadsImagesAutomatically = true //支持自动加载图片
//        settings.blockNetworkImage = true //解决图片不显示
        // 这样的话如果你的PC网页里面没有设置 meta标签 viewport的缩放设置也没有关系。
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.mediaPlaybackRequiresUserGesture = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = false
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                isLoaded = newProgress == 100
                Log.i(TAG, "onProgressChanged: $newProgress")
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.i(TAG, "onConsole: ${consoleMessage?.message()}")
                return super.onConsoleMessage(consoleMessage)
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                handler.proceed()
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                Log.i(TAG, "onLoadResource: $url")
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.i(TAG, "onReceivedError: ${request?.url} ${error?.toString()}")
                super.onReceivedError(view, request, error)
            }
        }
        webView.addJavascriptInterface(MoWallpaper(), "MoWallpaper")
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        val width = engine.desiredWidth
        val height = engine.desiredHeight
        webView.measure(width, height)
        webView.layout(0, 0, width, height)

//        val url = "${engine.context.getExternalFilesDir("")}/anim.html"
        val url = "file:///android_asset/one.html"
        Log.i(TAG, "onCreate: $url")
        webView.loadUrl(url)
    }

    open inner class MoWallpaper {

        @JavascriptInterface
        open fun animFrame() {
//            drawFrame()
        }
        @JavascriptInterface
        open fun getDir(): String? {
            return "file:///android_asset/"
        }
    }

    override fun onFrame(canvas: Canvas) {
        if (isLoaded) {
            webView.draw(canvas);
        } else {
            canvas.drawColor(Color.BLACK)
        }
    }
}
