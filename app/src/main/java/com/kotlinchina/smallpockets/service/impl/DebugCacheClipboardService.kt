package com.kotlinchina.smallpockets.service.impl

import com.kotlinchina.smallpockets.service.ClipboardService

class DebugCacheClipboardService: ClipboardService {
    override fun content(): String {
        val urls = listOf(
                "http://pinyin.sogou.com",
                "https://jinshuju.net",
                "http://www.a-li.com.cn",
                "https://realm.io",
                "http://zhihu.com",
                "http://youku.com"
        )
        val random = (Math.random() * urls.count()).toInt()
        return urls[random]
    }
}
