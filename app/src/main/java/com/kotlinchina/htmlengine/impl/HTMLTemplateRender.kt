package com.kotlinchina.htmlengine.impl

import com.kotlinchina.htmlengine.protocol.IHTMLTemplateRender

class HTMLTemplateRender: IHTMLTemplateRender {
    private val beforEachRegex: Regex
    private val valueRegex: Regex
    constructor() {
        this.beforEachRegex = Regex("\\@foreach \\(\\$([\\w]+)\\)([\\w\\W]+)\\@endforeach")
        this.valueRegex = Regex("\\$\\{(.+?)\\}")
    }

    override fun render(htmlTemplate: String, data: Map<String, Any>): String {
        val valueSymbols = valueRegex.findAll(htmlTemplate.toString())
        val subBeforEachSymbols = beforEachRegex.findAll(htmlTemplate.toString())

        var html = htmlTemplate
        subBeforEachSymbols.forEach { subBeforEachSymbol ->
            val subKey = subBeforEachSymbol.groupValues[1]
            val subhtmlTemplate = subBeforEachSymbol.groupValues[2]
            val subDatas = data[subKey] as? List<Map<String, Any>>
            var subHtml = ""
            subDatas?.forEach { data ->
                subHtml += render(subhtmlTemplate, data)
            }
            html = html.replace(subBeforEachSymbol.groupValues[0], subHtml)
        }
        valueSymbols.forEach { valueSymbol ->
            val valueSymbolKey = valueSymbol.groupValues[1]
            val valueSymbolText = valueSymbol.groupValues[0]
            val value = data[valueSymbolKey]
            html = html.replace(valueSymbolText, value.toString())
        }
        return html
    }
}
