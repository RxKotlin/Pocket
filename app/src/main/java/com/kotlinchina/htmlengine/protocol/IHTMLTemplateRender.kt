package com.kotlinchina.htmlengine.protocol

interface IHTMLTemplateRender {
    fun render(htmlTemplate: String, data: Map<String, Any>) : String
}
