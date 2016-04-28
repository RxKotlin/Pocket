package com.kotlinchina.smallpockets.service

import rx.Observable

interface ShareService {
    fun share(title: String, content: String): Observable<String>
}