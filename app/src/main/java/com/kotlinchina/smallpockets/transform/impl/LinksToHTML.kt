package com.kotlinchina.smallpockets.transform.impl

import android.content.Context
import com.kotlinchina.htmlengine.HTMLEngineManager
import com.kotlinchina.smallpockets.model.Link
import com.kotlinchina.smallpockets.model.impl.toMap
import com.kotlinchina.smallpockets.transform.ILinksToHTML

class LinksToHTMLWithHTMLEngine: ILinksToHTML {
    private val context: Context
    constructor(context: Context) {
        this.context = context
    }
    override fun html(links: List<Link>): String? {
        val manager = HTMLEngineManager(context, null, null)
        return manager.render("www/template.html", links.toMap())
    }
}
