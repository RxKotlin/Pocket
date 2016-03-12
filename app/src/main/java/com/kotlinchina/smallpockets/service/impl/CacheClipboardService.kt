package com.kotlinchina.smallpockets.service.impl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.kotlinchina.smallpockets.BuildConfig
import com.kotlinchina.smallpockets.service.ClipboardService

class CacheClipboardService: ClipboardService {
    private val context: Context

    constructor(context: Context) {
        this.context = context
    }

    override fun content(): String {
        val clipboardManager: ClipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var resultString: String = ""

        if (clipboardManager.hasPrimaryClip()) {
            val clipData: ClipData = clipboardManager.primaryClip
            val clipDataCount = clipData.itemCount
            for (index in 0..clipDataCount - 1) {
                val item = clipData.getItemAt(index)
                val str = item.coerceToText(context)
                resultString += str
            }
        }

        if (BuildConfig.DEBUG) {
            return "http://www.baidu.com"
        } else {
            return resultString
        }
    }
}