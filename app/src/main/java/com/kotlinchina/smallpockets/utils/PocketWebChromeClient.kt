package com.kotlinchina.smallpockets.utils

import android.webkit.WebChromeClient
import android.webkit.WebView

class PocketWebChromeClient: WebChromeClient() {
    var titleDidReceived: ((title: String) -> Unit)? = null
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        if (title == null) {
            return
        }
        titleDidReceived?.invoke(title)
    }
}
