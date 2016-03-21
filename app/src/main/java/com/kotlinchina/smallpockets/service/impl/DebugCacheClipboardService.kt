package com.kotlinchina.smallpockets.service.impl

import com.kotlinchina.smallpockets.service.ClipboardService

class DebugCacheClipboardService: ClipboardService {
    override fun content(): String {
        return "https://www.docker.com/"
    }
}