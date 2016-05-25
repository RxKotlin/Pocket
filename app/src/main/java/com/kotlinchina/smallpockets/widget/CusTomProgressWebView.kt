package com.kotlinchina.smallpockets.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
/**
 * Created by junjun
 */
import com.kotlinchina.smallpockets.R

class CustomProgressWebView : LinearLayout {

    private var mWebView: WebView? = null

    private var mProgressBar: ProgressBar? = null

    constructor(context: Context?) : this(context,null!!){}

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context!!) ;
    }

    private fun initView(context: Context) {
        val view = View.inflate(context, R.layout.view_web_progress, this)
        mWebView = view?.findViewById(R.id.web_view) as? WebView
        mProgressBar = view?.findViewById(R.id.progress_bar) as? ProgressBar
    }

    fun loadUrl(url: String?) {
        var url = url
        if (url == null) {
            url = "http://www.baidu.com"
        }
        initWebview(url)
    }

    private fun initWebview(url: String) {

        mWebView?.addJavascriptInterface(this, "android")
        val webSettings = mWebView?.settings
        webSettings?.javaScriptEnabled = true
        // 设置可以访问文件
        webSettings?.allowFileAccess = true
        // 设置可以支持缩放
        webSettings?.setSupportZoom(true)
        // 设置默认缩放方式尺寸是far
        webSettings?.defaultZoom = WebSettings.ZoomDensity.MEDIUM
        // 设置出现缩放工具
        webSettings?.builtInZoomControls = false
        webSettings?.defaultFontSize = 16
        mWebView?.loadUrl(url)
        // 设置WebViewClient
        mWebView?.setWebViewClient(object : WebViewClient() {
            // url拦截
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                view?.loadUrl(url)
                // 相应完成返回true
                return true
                // return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面开始加载
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mProgressBar?.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            // 页面加载完成
            override fun onPageFinished(view: WebView?, url: String?) {
                mProgressBar?.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            // WebView加载的所有资源url
            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String, failingUrl: String) {
                //				view.loadData(errorHtml, "text/html; charset=UTF-8", null);
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

        })

        // 设置WebChromeClient
        mWebView?.setWebChromeClient(object : WebChromeClient() {
            override // 处理javascript中的alert
            fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override // 处理javascript中的confirm
            fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            override // 处理javascript中的prompt
            fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            // 设置网页加载的进度条
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                mProgressBar?.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            // 设置程序的Title
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }
        })
        mWebView?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!!.canGoBack()) {
                    // 表示按返回键
                    mWebView?.goBack() // 后退
                    // webview.goForward();//前进
                    return@OnKeyListener true // 已处理
                }
            }
            false
        })
    }

    fun canBack(): Boolean {
        if (mWebView!!.canGoBack()) {
            mWebView?.goBack()
            return false
        }
        return true
    }
}
