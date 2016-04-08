package com.kotlinchina.htmlengine

import android.content.Context
import com.kotlinchina.htmlengine.impl.HTMLTemplateLoader
import com.kotlinchina.htmlengine.impl.HTMLTemplateRender
import com.kotlinchina.htmlengine.protocol.IHTMLTemplateLoader
import com.kotlinchina.htmlengine.protocol.IHTMLTemplateRender

class HTMLEngineManager<T: IHTMLTemplateLoader, U: IHTMLTemplateRender> {
    private val context: Context
    private val loader: T
    private val render: U

    constructor(context: Context, loader: T? = null, render: U? = null) {
        this.context = context
        this.loader = loader ?: HTMLTemplateLoader(context) as T
        this.render = render?: HTMLTemplateRender() as U
    }

    fun render(path: String, data: Map<String, Any>): String? {
        val htmlTemplate = loader.load(path) ?: return null
        return render.render(htmlTemplate, data)
    }
}
