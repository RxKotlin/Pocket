package com.kotlinchina.smallpockets.service

import rx.Observable

/**
 * Created by jizhang on 1/29/16.
 */
interface HttpService {
    fun fetchDataWithUrl(url: String): Observable<String>
}