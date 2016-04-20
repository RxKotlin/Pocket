package com.kotlinchina.smallpockets.utils

import android.webkit.WebView
import android.webkit.WebViewClient

class PocketWebViewClient: WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        view?.loadUrl(url)
        return true
    }
}
