package com.kotlinchina.smallpockets.service

import com.kotlinchina.smallpockets.model.Link
import rx.Observable

interface StoreService {
    fun store(title: String, content: String): Observable<Link>
    fun storeWeekly(links: List<Link>): Observable<String>
}