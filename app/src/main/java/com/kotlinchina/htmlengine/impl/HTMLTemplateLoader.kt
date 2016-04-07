package com.kotlinchina.htmlengine.impl

import android.content.Context
import android.util.Log
import com.kotlinchina.htmlengine.protocol.IHTMLTemplateLoader
import org.apache.commons.io.IOUtils

class HTMLTemplateLoader: IHTMLTemplateLoader {
    private val context: Context
    constructor(context: Context) {
        this.context = context
    }

    override fun load(path: String): String? {
        try {
            val inputStream = context.assets.open(path)
            return IOUtils.toString(inputStream, "UTF-8")
        } catch(e: Exception) {
            Log.e("${this.javaClass}", "The template can not be load")
        }
        return null
    }
}
