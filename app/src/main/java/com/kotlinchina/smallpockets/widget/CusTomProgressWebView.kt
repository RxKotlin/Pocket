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
    constructor(context: Context?) : this(context,null){}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView(context)
    }

    private fun initView(context: Context?) {
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
        initBaseSetting(url)
        setWebViewClient()
        setWebchromeClient()
        webViewOnkeyListener()
    }

    private fun initBaseSetting(url: String) {
        mWebView?.addJavascriptInterface(this, "android")
        val webSettings = mWebView?.settings
        webSettings?.javaScriptEnabled = true
        webSettings?.allowFileAccess = true
        webSettings?.setSupportZoom(true)
        webSettings?.defaultZoom = WebSettings.ZoomDensity.MEDIUM
        webSettings?.builtInZoomControls = false
        webSettings?.defaultFontSize = 16
        mWebView?.loadUrl(url)
    }

    private  fun setWebViewClient(){
        mWebView?.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mProgressBar?.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mProgressBar?.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String, failingUrl: String) {
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

        })
    }

    private fun setWebchromeClient() {
        mWebView?.setWebChromeClient(object : WebChromeClient() {
            override
            fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsAlert(view, url, message, result)
            }

            override
            fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
                return super.onJsConfirm(view, url, message, result)
            }

            override
            fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
                return super.onJsPrompt(view, url, message, defaultValue, result)
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                mProgressBar?.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
            }
        })
    }

    private fun webViewOnkeyListener() {
        mWebView?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val canGoBack = mWebView?.canGoBack() as? Boolean
                if (keyCode == KeyEvent.KEYCODE_BACK && canGoBack!=null && canGoBack) {
                    mWebView?.goBack()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

}
