package com.kotlinchina.smallpockets.service.impl

import android.content.Context
import android.util.Log
import com.kotlinchina.smallpockets.service.IHTMLTemplateRender
import org.apache.commons.io.IOUtils

class HTMLTemplateRender: IHTMLTemplateRender {
    private val path: String
    private val context: Context
    private val beforEachRegex: Regex
    private val valueRegex: Regex
    private var htmlTemplate: String? = null
    constructor(path: String, context: Context) {
        this.path = path
        this.context = context
        this.beforEachRegex = Regex("\\@foreach \\(\\$([\\w]+)\\)([\\w\\W]+)\\@endforeach")
        this.valueRegex = Regex("\\$\\{(.+?)\\}")
    }

    override fun render(data: Map<String, Any>): String? {
        try {
            load()
        } catch (e: Exception) {
            return null
        }

        return render(htmlTemplate!!, data)
    }

    private fun load() {
        try {
            val inputStream = context.assets.open(path)
            htmlTemplate = IOUtils.toString(inputStream, "UTF-8")
        } catch(e: Exception) {
            Log.e("${this.javaClass}", "The template can not be load")
        }
    }

    private fun render(htmlTemplate: String, data: Map<String, Any>): String {
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
