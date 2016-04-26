package com.kotlinchina.smallpockets.service

import rx.Observable

interface StoreService {
    fun store(title: String, content: String): Observable<String>
}