package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import android.webkit.WebView
import com.kotlinchina.smallpockets.service.HttpService
import com.kotlinchina.smallpockets.utils.PocketWebChromeClient
import com.kotlinchina.smallpockets.utils.PocketWebViewClient
import rx.Observable
import rx.lang.kotlin.observable

class WebViewClientHttpService: HttpService {
    var webview: WebView
    val context: Context
    val client: PocketWebChromeClient

    constructor(context: Context) {
        this.webview = WebView(context)
        this.client = PocketWebChromeClient()
        this.webview.setWebChromeClient(this.client)
        this.webview.setWebViewClient(PocketWebViewClient())
        this.context = context
    }

    override fun fetchDataWithUrl(url: String): Observable<String> {
        webview.loadUrl(url)
        return observable { subscriber ->
            client.titleDidReceived = { title ->
                subscriber.onNext(title)
                subscriber.onCompleted()
            }
        }
    }
}
