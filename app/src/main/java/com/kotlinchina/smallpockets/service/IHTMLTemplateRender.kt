package com.kotlinchina.smallpockets.service

interface IHTMLTemplateRender {
    fun render(data: Map<String, Any>) : String?
}
