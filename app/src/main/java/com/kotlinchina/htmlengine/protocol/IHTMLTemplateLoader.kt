package com.kotlinchina.htmlengine.protocol

interface IHTMLTemplateLoader {
    fun load(path: String): String?
}
